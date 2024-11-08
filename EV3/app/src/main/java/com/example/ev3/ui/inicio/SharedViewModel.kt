package com.example.ev3.ui.inicio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ev3.ui.informacion.InformacionViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    // Tiempo restante de carga en segundos
    private val _tiempoRestante = MutableLiveData<Int>()
    val tiempoRestante: LiveData<Int> get() = _tiempoRestante

    // Propiedad para el estado del cargador
    private val _chargerStatus = MutableLiveData<String>()
    val chargerStatus: LiveData<String> get() = _chargerStatus

    // Ahorro de CO₂ en gramos
    private val _ahorroCO2 = MutableLiveData<Double>()
    val ahorroCO2: LiveData<Double> get() = _ahorroCO2

    // Costo estimado de la carga en colones
    private val _costoCarga = MutableLiveData<Double>()
    val costoCarga: LiveData<Double> get() = _costoCarga

    // Método para actualizar el estado del cargador
    fun updateChargerStatus(status: String) {
        _chargerStatus.value = status
    }

    // Progreso de la carga (de 0 a 1)
    private val _progresoCarga = MutableLiveData<Float>()
    val progresoCarga: LiveData<Float> get() = _progresoCarga

    private val database = FirebaseDatabase.getInstance().getReference("estacionCarga/estado")
    private val informacionViewModel = InformacionViewModel() // Instancia de InformacionViewModel

    // Método para verificar el estado del cargador
    fun verificarEstadoCargador() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _chargerStatus.value = snapshot.getValue(String::class.java) ?: "Desconocido"
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de error (opcional)
                Log.e("SharedViewModel", "Error al obtener el estado del cargador: ${error.message}")
            }
        })
    }

    // Método para iniciar la carga si el cargador está disponible
    fun iniciarCargaSiDisponible() {
        if (_chargerStatus.value == "Disponible") {
            iniciarCarga()
        } else {
            // Opcional: manejar el caso en que no está disponible (ej. mostrar un mensaje)
        }
    }

    private fun iniciarCarga() {
        database.setValue("En uso").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _chargerStatus.value = "En uso"
                _tiempoRestante.value = 60
                _ahorroCO2.value = 0.0
                _costoCarga.value = 0.0
                _progresoCarga.value = 0f

                calcularAhorroCO2()
                calcularCostoCarga()

                viewModelScope.launch {
                    while (_tiempoRestante.value!! > 0) {
                        delay(1000)
                        _tiempoRestante.value = (_tiempoRestante.value?.minus(1))?.coerceAtLeast(0)
                        _progresoCarga.value = (60 - _tiempoRestante.value!!).toFloat() / 60
                    }
                    // Al finalizar la carga, guardar los datos de CO2
                    informacionViewModel.guardarDatosCO2(_ahorroCO2.value ?: 0.0)
                    liberarCarga()
                }
            }
        }
    }

    private fun calcularAhorroCO2() {
        // Datos estimados
        val potenciaCargador = 1.003  // en kW
        val factorEmisiones = 53  // en gramos de CO₂ por kWh

        // Declarar la variable acumulativa fuera del bucle para que retenga el valor entre ciclos
        var ahorroAcumulado = _ahorroCO2.value ?: 0.0

        viewModelScope.launch {
            while (_tiempoRestante.value!! > 0) {
                delay(1000)

                // Calcular el ahorro de CO₂ en gramos por segundo
                val ahorroPorSegundo = (potenciaCargador * factorEmisiones) / 3600  // en gramos

                // Sumar al ahorro acumulado (sin reiniciar en cada ciclo)
                ahorroAcumulado += ahorroPorSegundo

                // Actualizar `_ahorroCO2` con el ahorro acumulado (sin formato en cada paso)
                _ahorroCO2.value = ahorroAcumulado
            }

            // Al final del cálculo, formatear con 1 decimal para la visualización final
            _ahorroCO2.value = "%.1f".format(ahorroAcumulado).toDouble()
        }
    }


    private fun calcularCostoCarga() {
        // Variables de tarifas de ejemplo (en colones por kWh) para cada período. Ajustar según el TEC o tarifas actuales.
        val tarifaPico = 120.0 / 3600  // Dividir entre 3600 para obtener tarifa por segundo
        val tarifaValle = 80.0 / 3600
        val tarifaNocturna = 60.0 / 3600

        // Función para determinar la tarifa según el horario actual
        fun obtenerTarifaActual(): Double {
            val hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            return when {
                hora in 10..12 || hora in 17..20 -> tarifaPico
                hora in 6..9 || hora in 13..17 -> tarifaValle
                else -> tarifaNocturna
            }
        }

        _costoCarga.value = 0.0
        viewModelScope.launch {
            while (_tiempoRestante.value!! > 0) {
                delay(1000)  // Espera de 1 segundo
                val tarifaActual = obtenerTarifaActual()
                _costoCarga.value = _costoCarga.value?.plus(tarifaActual)
            }
        }
    }

    private fun liberarCarga() {
        database.setValue("Disponible").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _chargerStatus.value = "Disponible"
            }
        }
    }
}