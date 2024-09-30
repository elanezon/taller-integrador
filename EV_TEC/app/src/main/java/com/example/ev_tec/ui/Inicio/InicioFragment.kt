package com.example.ev_tec.ui.Inicio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ev_tec.databinding.FragmentInicioBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class InicioFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    private var chargerAvailable = false // Simulación de disponibilidad del cargador

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Simular actualización de la disponibilidad del cargador
        updateChargerStatus()

        // Configurar el mapa
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configurar el botón de iniciar carga
        binding.startChargeButton.setOnClickListener {
            if (chargerAvailable) {
                startCharging()
            } else {
                Toast.makeText(requireContext(), "El cargador no está disponible", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun updateChargerStatus() {
        // Simulación de datos dinámicos. Podrías obtener esto de una API.
        chargerAvailable = true // Supongamos que el cargador está disponible

        if (chargerAvailable) {
            binding.chargerStatusText.text = "Cargador disponible"
            binding.startChargeButton.isEnabled = true
        } else {
            binding.chargerStatusText.text = "Cargador no disponible"
            binding.startChargeButton.isEnabled = false
        }
    }

    private fun startCharging() {
        // Aquí va la lógica para iniciar la carga
        Toast.makeText(requireContext(), "Carga iniciada", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Configuración básica del mapa y marcador
        val chargerLocation = LatLng(9.9333, -84.0833) // Coordenadas de ejemplo (San José, Costa Rica)
        googleMap.addMarker(MarkerOptions().position(chargerLocation).title("Cargador"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chargerLocation, 15f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
