package com.example.ev_tec.ui.Inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InicioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Inicio Fragment"
    }
    val text: LiveData<String> = _text
}