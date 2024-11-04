package com.example.ev3.ui.inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InicioViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Estado de carga: --" // Mensaje inicial
    }
    val text: LiveData<String> = _text

    // Método para actualizar el estado del cargador
    fun actualizarEstadoCargador(estado: String) {
        _text.value = "Estado de carga: $estado"
    }
}