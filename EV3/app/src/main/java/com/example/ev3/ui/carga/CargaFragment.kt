package com.example.ev3.ui.carga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentCargaBinding

class CargaFragment : Fragment() {

    private var _binding: FragmentCargaBinding? = null
    private val binding get() = _binding!!

    private lateinit var cargaViewModel: CargaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cargaViewModel = ViewModelProvider(this)[CargaViewModel::class.java]

        _binding = FragmentCargaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Referencias a los TextViews en el layout
        val timerTextView: TextView = binding.timerTextView
        val co2AhorroTextView: TextView = binding.co2AhorroTextView
        val costoTextView: TextView = binding.costoTextView

        // Observar los cambios en el tiempo restante
        cargaViewModel.tiempoRestante.observe(viewLifecycleOwner) { tiempoRestante ->
            val minutos = tiempoRestante / 60
            val segundos = tiempoRestante % 60
            timerTextView.text = "Tiempo restante: $minutos:$segundos"
        }

        // Observar los cambios en el ahorro de CO₂
        cargaViewModel.co2Ahorro.observe(viewLifecycleOwner) { co2Ahorro ->
            co2AhorroTextView.text = "CO₂ ahorrado: $co2Ahorro kg"
        }

        // Observar los cambios en el costo
        cargaViewModel.costo.observe(viewLifecycleOwner) { costo ->
            costoTextView.text = "Costo: $costo Colones"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}