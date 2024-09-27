package com.example.ev3.ui.usuario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsuarioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is usuario Fragment"
    }
    val text: LiveData<String> = _text
}