package com.example.ev3.ui.inicio

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentInicioBinding

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inicioViewModel =
            ViewModelProvider(this).get(InicioViewModel::class.java)

        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setear el texto del TextView que sí existe en el XML
        val textView: TextView = binding.chargerStatusTextView
        inicioViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Configurar el botón para abrir Google Maps
        val buttonOpenMaps: Button = binding.openMapsButton
        buttonOpenMaps.setOnClickListener {
            openGoogleMaps()  // Método para abrir Google Maps
        }

        // Configurar otros botones si es necesario
        val buttonCheckCharger: Button = binding.checkChargerButton
        buttonCheckCharger.setOnClickListener {
            // Lógica para verificar el estado del cargador
        }

        val buttonStartCharging: Button = binding.startChargingButton
        buttonStartCharging.setOnClickListener {
            // Lógica para iniciar la carga
        }

        return root
    }

    // Método para abrir Google Maps con una ubicación específica
    private fun openGoogleMaps() {
        // URI con el nombre de la ubicación
        val locationName = "EVTEC Station" // Cambia esto por el nombre deseado
        val gmmIntentUri = Uri.parse("geo:0,0?q=9.861828545473676,-83.91554636862311($locationName)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        // Intenta iniciar la actividad sin especificar el paquete
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Google Maps no está disponible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}