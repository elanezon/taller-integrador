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
    private lateinit var database: DatabaseReference
    private val handler = Handler(Looper.getMainLooper())
    private val liberarCargaRunnable = Runnable {
        liberarCarga()
    }

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

        // Inicializar la Realtime Database
        database = FirebaseDatabase.getInstance().getReference("estacionCarga/estado")

        // Observa el estado del cargador en el SharedViewModel y actualiza el TextView
        sharedViewModel.chargerStatus.observe(viewLifecycleOwner) { status ->
            binding.chargerStatusTextView.text = "Estado de carga: $status"
        }

        // Configurar el botón para abrir Google Maps
        binding.openMapsButton.setOnClickListener {
            openGoogleMaps()
        }

        // Configurar el botón para verificar el estado del cargador
        binding.checkChargerButton.setOnClickListener {
            verificarEstadoCargador()
        }

        // Configurar el botón para iniciar carga
        binding.startChargingButton.setOnClickListener {
            iniciarCarga()
            // Después de iniciar la carga, guarda el ahorro de CO₂ en la base de datos
            sharedViewModel.ahorroCO2.observe(viewLifecycleOwner) { ahorroCO2 ->
                informacionViewModel.guardarDatosCO2(ahorroCO2)
            }
        }

        return root
    }

    private fun verificarEstadoCargador() {
        // Obtener el estado del cargador desde la base de datos
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(String::class.java)
                sharedViewModel.updateChargerStatus(estado ?: "Desconocido")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al verificar estado", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun iniciarCarga() {
        // Actualizar el estado de la base de datos a "En uso"
        database.setValue("En uso").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Carga iniciada", Toast.LENGTH_SHORT).show()
                sharedViewModel.updateChargerStatus("En uso")
                sharedViewModel.iniciarCarga()
                programarLiberacionCarga()
            } else {
                Toast.makeText(requireContext(), "Error al iniciar carga", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun programarLiberacionCarga() {
        handler.postDelayed(liberarCargaRunnable, 60000)
    }

    private fun liberarCarga() {
        database.setValue("Disponible").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (_binding != null) {
                    Toast.makeText(requireContext(), "Carga liberada", Toast.LENGTH_SHORT).show()
                    sharedViewModel.updateChargerStatus("Disponible")
                }
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(liberarCargaRunnable)
    }
}

