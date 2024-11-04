package com.example.ev3.ui.usuario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ev3.R
import com.example.ev3.databinding.FragmentUsuarioBinding
import com.example.ev3.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class UsuarioFragment : Fragment() {

    private var _binding: FragmentUsuarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var usuarioViewModel: UsuarioViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        usuarioViewModel = ViewModelProvider(requireActivity())[UsuarioViewModel::class.java]

        _binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Configurar Google Sign-In options para obtener el GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile() // Solicitar acceso al perfil para obtener la foto
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Obtener la cuenta de Google iniciada
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        // Obtener datos de la actividad principal
        val email = activity?.intent?.getStringExtra("USER_EMAIL")
        val codigo = activity?.intent?.getStringExtra("USER_CODE")

        // Mostrar el correo y el código
        binding.textUsuarioCorreo.text = "Correo: $email"
        binding.textUsuarioCodigo.text = "Código: $codigo"

        // Mostrar la imagen de perfil usando Glide
        account?.let {
            // Mostrar la imagen de perfil
            val photoUrl = it.photoUrl
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_user) // Imagen por defecto
                    .into(binding.imageUsuario)
            } else {
                binding.imageUsuario.setImageResource(R.drawable.ic_default_user) // Imagen por defecto
            }
        }

        // Configurar el botón de cierre de sesión
        binding.buttonLogout.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(activity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}