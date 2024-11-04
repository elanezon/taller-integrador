package com.example.ev3.ui.usuario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsuarioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    // LiveData para almacenar el correo del usuario
    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    // LiveData para el código asociado
    private val _codigo = MutableLiveData<String>()
    val codigo: LiveData<String> = _codigo

    // Método para actualizar el correo del usuario
    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    // Método para actualizar el código
    fun setCodigo(Codigo: String) {
        _codigo.value = Codigo
    }
}