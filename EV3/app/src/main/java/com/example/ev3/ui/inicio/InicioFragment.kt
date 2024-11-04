package com.example.ev3.ui.inicio

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentInicioBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inicioViewModel =
            ViewModelProvider(this).get(InicioViewModel::class.java)

        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializar la Realtime Database
        database = FirebaseDatabase.getInstance().getReference("estacionCarga/estado")

        // Establecer el texto inicial del TextView
        binding.chargerStatusTextView.text = "Estado de carga: --"

        // Configurar el botón para abrir Google Maps
        val buttonOpenMaps: Button = binding.openMapsButton
        buttonOpenMaps.setOnClickListener {
            openGoogleMaps()  // Método para abrir Google Maps
        }

        // Configurar el botón para verificar el estado del cargador
        val buttonCheckCharger: Button = binding.checkChargerButton
        buttonCheckCharger.setOnClickListener {
            verificarEstadoCargador() // Lógica para verificar el estado del cargador
        }

        // Configurar el botón para iniciar carga
        val buttonStartCharging: Button = binding.startChargingButton
        buttonStartCharging.setOnClickListener {
            iniciarCarga() // Lógica para iniciar la carga
        }

        return root
    }

    private fun verificarEstadoCargador() {
        // Obtener el estado del cargador desde la base de datos
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(String::class.java)
                binding.chargerStatusTextView.text = "Estado de carga: $estado"
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
                binding.chargerStatusTextView.text = "Estado de carga: En uso" // Actualizar el TextView
                programarLiberacionCarga()
            } else {
                Toast.makeText(requireContext(), "Error al iniciar carga", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun programarLiberacionCarga() {
        // Liberar el estado de carga después de 1 minuto (60000 ms)
        Handler(Looper.getMainLooper()).postDelayed({
            database.setValue("Disponible").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Carga liberada", Toast.LENGTH_SHORT).show()
                    binding.chargerStatusTextView.text = "Estado de carga: Disponible" // Actualizar el TextView
                }
            }
        }, 10000) // Cambia este valor si deseas una duración diferente
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
