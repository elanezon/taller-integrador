package com.example.ev_tec.ui.General

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneralViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is General Fragment"
    }
    val text: LiveData<String> = _text
}