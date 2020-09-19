package com.money.budget.wealthy.ui.expenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedExpenseViewModel : ViewModel() {

    val toolbarCalendar = MutableLiveData<Any>()

    fun setToolbarCalendar(toolbarCalendar: String) {
        this.toolbarCalendar.value = toolbarCalendar
    }
}
