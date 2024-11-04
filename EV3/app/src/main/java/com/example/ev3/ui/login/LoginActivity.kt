package com.example.ev3.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.MainActivity
import com.example.ev3.R
import com.example.ev3.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference // Referencia a Realtime Database
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar Realtime Database
        db = FirebaseDatabase.getInstance().getReference("usuarios")

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Obtén el ID del cliente desde `google-services.json`
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializar vista
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el botón de inicio de sesión de Google
        binding.googleSignInButton?.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, obtener el usuario
                    val user = auth.currentUser
                    user?.let {
                        val email = it.email
                        val uid = it.uid // UID del usuario para identificarlo en Realtime Database
                        val photoUrl = it.photoUrl?.toString()

                        if (email != null) {
                            // Revisar y obtener el código único en Realtime Database
                            db.child(uid).get()
                                .addOnSuccessListener { snapshot ->
                                    var codigoUnico: String

                                    if (snapshot.exists() && snapshot.child("codigo").value != null) {
                                        // Si ya existe un código, obtén el valor
                                        codigoUnico = snapshot.child("codigo").value.toString()
                                    } else {
                                        // Generar y guardar un nuevo código
                                        codigoUnico = generarCodigoUnico()
                                        val usuarioData = mapOf(
                                            "codigo" to codigoUnico,
                                            "email" to email,
                                            "photoUrl" to photoUrl
                                        )
                                        db.child(uid).setValue(usuarioData)
                                            .addOnSuccessListener {
                                                Log.d("LoginActivity", "Código y datos de usuario guardados.")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("LoginActivity", "Error al guardar datos: ", e)
                                            }
                                    }

                                    // Pasar el correo y el código a MainActivity
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.putExtra("USER_EMAIL", email)
                                    intent.putExtra("USER_CODE", codigoUnico)
                                    intent.putExtra("USER_PHOTO_URL", photoUrl)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.w("LoginActivity", "Error al obtener o crear el código", e)
                                }
                        } else {
                            Log.e("LoginActivity", "El email del usuario es null.")
                        }
                    } ?: Log.e("LoginActivity", "Error: el usuario es null.")
                } else {
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }

    // Función para generar un código único
    private fun generarCodigoUnico(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..8)
            .map { charset.random() }
            .joinToString("")
    }
}
