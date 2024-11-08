package com.example.ev3.ui.carga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentCargaBinding
import com.example.ev3.ui.inicio.SharedViewModel

class CargaFragment : Fragment() {

    private var _binding: FragmentCargaBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel // Cambia a SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCargaBinding.inflate(inflater, container, false)

        // Obtén una instancia del SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Rotar el ProgressBar 180 grados para que empiece desde la parte izquierda
        binding.progressBar.rotation = -180f

        // Observa el progreso de la carga y actualiza la barra de progreso
        sharedViewModel.progresoCarga.observe(viewLifecycleOwner) { progreso ->
            // Actualiza la barra de progreso en la interfaz de usuario
            binding.progressBar.progress = (progreso * 100).toInt()  // Asegúrate que el rango de la barra sea de 0 a 100
            binding.percentageTextView.text = "${(progreso * 100).toInt()}%"  // Muestra el porcentaje
        }

        // Observa el tiempo restante y actualiza la UI
        sharedViewModel.tiempoRestante.observe(viewLifecycleOwner) { tiempo ->
            binding.timeRemainingTextView.text = "Tiempo restante: ${tiempo / 60}:${tiempo % 60}"
        }

        // Observa el ahorro de CO₂ y actualiza la UI
        sharedViewModel.ahorroCO2.observe(viewLifecycleOwner) { co2 ->
            binding.co2SavingsTextView.text = "CO₂ Ahorrado: ${co2}mg"
        }

        // Observa el costo estimado y actualiza la UI
        sharedViewModel.costoCarga.observe(viewLifecycleOwner) { costo ->
            binding.costTextView.text = "Costo estimado: ₡${String.format("%.2f", costo)}"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
