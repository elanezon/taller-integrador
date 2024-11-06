package com.example.ev3.ui.inicio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun iniciarCarga() {
        // Configura un tiempo de carga de 1 minutos (60 segundos) para esta simulación
        _tiempoRestante.value = 60
        _ahorroCO2.value = 0.0
        _costoCarga.value = 0.0
        _progresoCarga.value = 0f // Inicia en 0% de carga
        calcularAhorroCO2()
        calcularCostoCarga()

        // Reduce el tiempo restante cada segundo para simular la carga
        viewModelScope.launch {
            while (_tiempoRestante.value!! > 0) {
                delay(1000)
                _tiempoRestante.value = (_tiempoRestante.value?.minus(1))?.coerceAtLeast(0)
                // Actualizar progreso de carga cada segundo
                val progreso = (60 - _tiempoRestante.value!!).toFloat() / 60
                _progresoCarga.value = progreso
            }
        }
    }

    private fun calcularAhorroCO2() {
        // Supongamos que se ahorran 0.1 gramos de CO₂ por segundo de carga
        _ahorroCO2.value = 0.0
        viewModelScope.launch {
            while (_tiempoRestante.value!! > 0) {
                delay(1000)
                _ahorroCO2.value = (_ahorroCO2.value?.plus(0.1))?.let { "%.1f".format(it).toDouble() }
            }
        }
    }

    private fun calcularCostoCarga() {
        // Supongamos que el costo es de 0.5 colones por segundo de carga
        _costoCarga.value = 0.0
        viewModelScope.launch {
            while (_tiempoRestante.value!! > 0) {
                delay(1000)
                _costoCarga.value = _costoCarga.value?.plus(0.5)
            }
        }
    }
}