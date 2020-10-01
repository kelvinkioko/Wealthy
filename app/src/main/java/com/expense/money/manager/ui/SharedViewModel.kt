package com.expense.money.manager.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    val reload = MutableLiveData<Any>()

    fun setReload(reload: Boolean) {
        this.reload.value = reload
    }
}
