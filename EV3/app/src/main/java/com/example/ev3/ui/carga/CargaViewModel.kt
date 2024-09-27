package com.example.ev3.ui.carga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.timer

class CargaViewModel : ViewModel() {

    // LiveData para el tiempo restante
    private val _tiempoRestante = MutableLiveData<Int>()
    val tiempoRestante: LiveData<Int> get() = _tiempoRestante

    // LiveData para el ahorro de CO₂
    private val _co2Ahorro = MutableLiveData<Double>()
    val co2Ahorro: LiveData<Double> get() = _co2Ahorro

    // LiveData para el costo
    private val _costo = MutableLiveData<Double>()
    val costo: LiveData<Double> get() = _costo

    private var tiempoInicial: Int = 3600 // 1 hora en segundos

    init {
        iniciarTemporizador()
        actualizarCalculos()
    }

    private fun iniciarTemporizador() {
        // Configura un temporizador que actualiza el tiempo restante cada segundo
        _tiempoRestante.value = tiempoInicial
        val timer = timer(period = 1000) {
            val tiempoActual = _tiempoRestante.value ?: tiempoInicial
            if (tiempoActual > 0) {
                _tiempoRestante.postValue(tiempoActual - 1)
            } else {
                cancel() // Detener el temporizador cuando llegue a 0
            }
        }
    }

    private fun actualizarCalculos() {
        // Lógica simplificada para calcular el ahorro de CO₂ y el costo
        _co2Ahorro.value = 1.5 // Ahorro de CO₂ simulado
        _costo.value = 2.50 // Costo simulado en USD
    }
}