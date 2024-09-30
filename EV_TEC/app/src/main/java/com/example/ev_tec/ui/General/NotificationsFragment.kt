package com.example.ev_tec.ui.General

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev_tec.databinding.FragmentGeneralBinding

class GeneralFragment : Fragment() {

    private var _binding: FragmentGeneralBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val GeneralViewModel =
            ViewModelProvider(this).get(GeneralViewModel::class.java)

        _binding = FragmentGeneralBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGeneral
        GeneralViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}