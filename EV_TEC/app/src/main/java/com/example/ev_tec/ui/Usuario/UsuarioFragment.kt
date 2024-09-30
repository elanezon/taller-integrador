package com.example.ev_tec.ui.Usuario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev_tec.databinding.FragmentUsuarioBinding

class UsuarioFragment : Fragment() {

    private var _binding: FragmentUsuarioBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val UsuarioViewModel: UsuarioViewModel =
            ViewModelProvider(this).get(UsuarioViewModel::class.java)

        _binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textUsuario
        UsuarioViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}