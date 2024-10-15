package com.example.ev3.ui.usuario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentUsuarioBinding
import com.example.ev3.ui.login.LoginActivity

class UsuarioFragment : Fragment() {

    private var _binding: FragmentUsuarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        usuarioViewModel = ViewModelProvider(requireActivity())[UsuarioViewModel::class.java]

        _binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
            // Volver a la actividad de Login
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            // Opcional: finalizar la actividad actual si no deseas que el usuario regrese a este fragmento
            requireActivity().finish()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}