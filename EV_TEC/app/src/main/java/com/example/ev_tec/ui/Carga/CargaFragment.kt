package com.example.ev_tec.ui.Carga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev_tec.databinding.FragmentCargaBinding

class CargaFragment : Fragment() {

    private var _binding: FragmentCargaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val CargaViewModel =
            ViewModelProvider(this).get(CargaViewModel::class.java)

        _binding = FragmentCargaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCarga
        CargaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}