package com.example.ev_tec.ui.Usuario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsuarioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Usuario Fragment"
    }
    val text: LiveData<String> = _text
}