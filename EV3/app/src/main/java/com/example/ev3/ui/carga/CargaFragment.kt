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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCargaBinding.inflate(inflater, container, false)
        cargaViewModel = ViewModelProvider(this).get(CargaViewModel::class.java)

        // Observa el tiempo restante y actualiza la UI
        cargaViewModel.tiempoRestante.observe(viewLifecycleOwner) { tiempo ->
            val porcentaje = ((600 - tiempo) / 600.0 * 100).toInt() // Calcula el porcentaje de progreso
            binding.progressBar.progress = porcentaje
            binding.percentageTextView.text = "$porcentaje%" // Actualiza el porcentaje mostrado en el centro
            binding.timeRemainingTextView.text = "Tiempo restante: ${tiempo / 60}:${tiempo % 60}"
        }


        // Observa el ahorro de CO₂ y actualiza la UI
        cargaViewModel.ahorroCO2.observe(viewLifecycleOwner) { co2 ->
            binding.co2SavingsTextView.text = "CO₂ Ahorrado: ${co2}g"
        }

        // Observa el costo estimado y actualiza la UI
        cargaViewModel.costoCarga.observe(viewLifecycleOwner) { costo ->
            binding.costTextView.text = "Costo estimado: ₡${String.format("%.2f", costo)}"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
