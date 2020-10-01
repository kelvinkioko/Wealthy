package com.expense.money.manager.ui.expenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedExpenseViewModel : ViewModel() {

    val toolbarYearCalendar = MutableLiveData<Any>()
    val toolbarMonthCalendar = MutableLiveData<Any>()

    fun setToolbarMonthCalendar(toolbarMonthCalendar: String) {
        this.toolbarMonthCalendar.value = toolbarMonthCalendar
    }

    fun setToolbarYearCalendar(toolbarYearCalendar: String) {
        this.toolbarYearCalendar.value = toolbarYearCalendar
    }
}
