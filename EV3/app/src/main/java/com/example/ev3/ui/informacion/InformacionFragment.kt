package com.example.ev3.ui.informacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentInformacionBinding


class InformacionFragment : Fragment() {

    private var _binding: FragmentInformacionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val informacionViewModel =
            ViewModelProvider(this)[InformacionViewModel::class.java]

        _binding = FragmentInformacionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textInformacion
        informacionViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}