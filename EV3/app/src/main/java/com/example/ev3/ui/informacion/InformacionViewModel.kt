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

class InformacionViewModel : ViewModel() {

    // MutableLiveData para contener los datos de CO₂ ahorrado
    private val _ahorrosCO2 = MutableLiveData<List<Entry>>()  // Lista de entradas para el gráfico
    val ahorrosCO2: LiveData<List<Entry>> get() = _ahorrosCO2

    private val myRef = FirebaseDatabase.getInstance().getReference("ahorro_CO2")

    init {
        obtenerDatosCO2()
    }

    // Función para guardar los datos de CO₂ en Firebase
    fun guardarDatosCO2(co2: Double) {
        val timestamp = System.currentTimeMillis()  // Usar el tiempo actual como identificador
        val data = mapOf("timestamp" to timestamp, "co2" to co2)

        myRef.child(timestamp.toString()).setValue(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("InformacionViewModel", "Datos CO₂ guardados correctamente.")
            } else {
                Log.e("InformacionViewModel", "Error al guardar datos: ${task.exception?.message}")
            }
        }
    }

    // Recupera los datos de Firebase y los convierte en una lista de "Entry" para el gráfico
    private fun obtenerDatosCO2() {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val entries = mutableListOf<Entry>()

                // Iterar sobre los datos recibidos desde Firebase
                for (data in snapshot.children) {
                    // Acceder al "mapa" dentro de cada nodo de Firebase
                    val co2 = data.child("co2").getValue(Double::class.java)  // Obtener el valor de CO₂
                    val timestamp = data.child("timestamp").getValue(Long::class.java)  // Obtener el valor de timestamp

                    // Si se obtiene un valor válido de CO₂ y un timestamp
                    if (co2 != null && timestamp != null) {
                        // Agregar la entrada al gráfico
                        entries.add(Entry(timestamp.toFloat(), co2.toFloat()))
                    }
                }

                // Actualizar la lista de entradas para el gráfico
                _ahorrosCO2.value = entries
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("InformacionViewModel", "Error al cargar los datos: ${error.message}")
            }
        })
    }
}