package com.example.ev3.ui.informacion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

class InformacionViewModel : ViewModel() {

    // MutableLiveData para contener los datos de CO₂ ahorrado
    private val _ahorrosCO2 = MutableLiveData<List<Entry>>()
    val ahorrosCO2: LiveData<List<Entry>> get() = _ahorrosCO2

    private val myRef = FirebaseDatabase.getInstance().getReference("ahorro_CO2")
    private var totalCO2 = 0.0  // Variable para almacenar el total acumulado

    init {
        obtenerDatosCO2()
    }

    // Función para guardar el CO₂ acumulativo en Firebase
    fun guardarDatosCO2(co2: Double) {
        // Actualizar el total acumulado de CO₂
        totalCO2 += co2

        val timestamp = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val fechaFormateada = sdf.format(Date(timestamp))

        val data = mapOf(
            "timestamp" to timestamp,
            "co2" to totalCO2,  // Guardar el total acumulado en lugar del incremento
            "fecha" to fechaFormateada
        )

        // Guardar el dato acumulativo en Firebase
        myRef.child(timestamp.toString()).setValue(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("InformacionViewModel", "Datos CO₂ guardados correctamente.")
            } else {
                Log.e("InformacionViewModel", "Error al guardar datos: ${task.exception?.message}")
            }
        }
    }

    // Recupera los datos acumulados de Firebase y los convierte en una lista de "Entry" para el gráfico
    private fun obtenerDatosCO2() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val entries = mutableListOf<Entry>()
                totalCO2 = 0.0  // Reiniciar el total acumulado

                for (data in snapshot.children) {
                    val co2 = data.child("co2").getValue(Double::class.java)
                    val timestamp = data.child("timestamp").getValue(Long::class.java)

                    if (co2 != null && timestamp != null) {
                        totalCO2 = co2  // Obtener el valor acumulado de Firebase
                        entries.add(Entry(timestamp.toFloat(), totalCO2.toFloat()))
                    }
                }

                _ahorrosCO2.value = entries
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("InformacionViewModel", "Error al cargar los datos: ${error.message}")
            }
        })
    }
}