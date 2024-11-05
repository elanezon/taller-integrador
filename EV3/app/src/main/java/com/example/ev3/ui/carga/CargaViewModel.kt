package com.example.ev3.ui.carga

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.timer

class CargaViewModel : ViewModel() {

    private val _tiempoRestante = MutableLiveData<Int>()
    val tiempoRestante: LiveData<Int> get() = _tiempoRestante

    private val _ahorroCO2 = MutableLiveData<Int>()
    val ahorroCO2: LiveData<Int> get() = _ahorroCO2

    private val _costoCarga = MutableLiveData<Double>()
    val costoCarga: LiveData<Double> get() = _costoCarga

    private var tiempoInicial = 600 // Tiempo de carga en segundos (10 minutos)
    private val ahorroCO2PorMinuto = 150 // CO₂ ahorrado por minuto en gramos
    private val tarifaElectricidadPorKWh = 90.0 // Tarifa en colones por kWh
    private val consumoKWhPorMinuto = 0.33 // Consumo estimado en kWh por minuto

    private val countDownTimer = object : CountDownTimer(tiempoInicial * 1000L, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val tiempoRestante = (millisUntilFinished / 1000).toInt()
            _tiempoRestante.value = tiempoRestante
            calcularAhorroYCosto(tiempoRestante)
        }

        override fun onFinish() {
            _tiempoRestante.value = 0
            _ahorroCO2.value = (tiempoInicial / 60.0 * ahorroCO2PorMinuto).toInt()
            _costoCarga.value = (tiempoInicial / 60.0 * consumoKWhPorMinuto * tarifaElectricidadPorKWh)
        }
    }

    init {
        // Inicializar valores
        _tiempoRestante.value = tiempoInicial
        _ahorroCO2.value = 0
        _costoCarga.value = 0.0
        countDownTimer.start()
    }

    private fun calcularAhorroYCosto(tiempoRestante: Int) {
        val minutosTranscurridos = (tiempoInicial - tiempoRestante) / 60.0
        _ahorroCO2.value = (minutosTranscurridos * ahorroCO2PorMinuto).toInt()
        _costoCarga.value = minutosTranscurridos * consumoKWhPorMinuto * tarifaElectricidadPorKWh
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer.cancel() // Detiene el temporizador si se destruye el ViewModel
    }
}
