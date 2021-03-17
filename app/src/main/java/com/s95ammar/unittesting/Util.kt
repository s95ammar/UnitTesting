package com.s95ammar.unittesting

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun EditText.setTextIfNotEquals(newText: String?) {
    if (text?.toString() != newText)
        setText(newText)
}