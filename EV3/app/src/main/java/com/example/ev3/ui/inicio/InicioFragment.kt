package com.example.ev3.ui.inicio

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentInicioBinding
import com.example.ev3.ui.informacion.InformacionViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    // Instancia del SharedViewModel
    private lateinit var sharedViewModel: SharedViewModel

    // Instancia del InformacionViewModel
    private lateinit var informacionViewModel: InformacionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        informacionViewModel = ViewModelProvider(requireActivity()).get(InformacionViewModel::class.java)

        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Llamar a la función para verificar el estado del cargador al cargar la vista
        sharedViewModel.verificarEstadoCargador()


        // Observa el estado del cargador y actualiza la interfaz
        sharedViewModel.chargerStatus.observe(viewLifecycleOwner) { status ->
            binding.chargerStatusTextView.text = "Estado de carga: $status"
            binding.startChargingButton.isEnabled = (status == "Disponible") // Habilitar solo si está disponible
        }

        // Configurar el botón para abrir Google Maps
        binding.openMapsButton.setOnClickListener {
            openGoogleMaps()
        }

        // Configurar el botón para iniciar carga
        binding.startChargingButton.setOnClickListener {
            sharedViewModel.iniciarCargaSiDisponible()
        }

        return root
    }


    private fun openGoogleMaps() {
        val locationName = "EVTEC Station"
        val gmmIntentUri = Uri.parse("geo:0,0?q=9.861828545473676,-83.91554636862311($locationName)")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
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

