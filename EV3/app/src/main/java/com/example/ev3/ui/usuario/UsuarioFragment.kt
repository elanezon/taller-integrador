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
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Obtener la cuenta de Google iniciada
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        account?.let {
            // Mostrar el correo
            binding.textUsuarioCorreo.text = "Correo: ${it.email}"

            // Mostrar la imagen de perfil usando Glide
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

        // Observar el correo del usuario y mostrarlo
        usuarioViewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.textUsuarioCorreo.text = "Correo: $email"  // Mostrar el correo con el prefijo "Correo: "
        }

        // Si también manejas un código, observarlo y mostrarlo
        usuarioViewModel.codigo.observe(viewLifecycleOwner) { codigo ->
            // Si tienes un TextView para el código, actualízalo aquí
            // binding.codigoTextView.text = "Código: $codigo"
        }

        // Configurar el botón de cierre de sesión
        binding.buttonLogout.setOnClickListener {
            // Cerrar sesión de Google
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Navegar a la actividad de Login
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish() // Finalizar la actividad actual para prevenir volver a este fragmento
                } else {
                    // Manejar el error si el cierre de sesión falla
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