package com.money.budget.wealthy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    val reload = MutableLiveData<Any>()

    fun setReload(reload: Boolean) {
        this.reload.value = reload
    }
}
