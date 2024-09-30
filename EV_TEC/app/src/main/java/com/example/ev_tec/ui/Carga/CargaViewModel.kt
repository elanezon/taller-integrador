package com.example.ev_tec.ui.Carga

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CargaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Carga Fragment"
    }
    val text: LiveData<String> = _text
}