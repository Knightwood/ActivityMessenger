package com.kiylx.activitymessage.activity_message_test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Main Activity"
    }
    val text: LiveData<String> = _text

    fun changeText(newText: String?) {
        _text.value = "返回值" + (newText ?: "没有返回值")
    }
}