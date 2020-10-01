package com.expense.money.manager.constants

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferenceHandler(private val activity: Activity) {

    private val sharedPrefFile = "WealthyPreference"

    private val sharedPreferences: SharedPreferences = activity.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    fun setCalendarMonth(monthSelection: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("monthSelection", monthSelection)
        editor.apply()
    }

    fun getCalendarMonth(): String? {
        return sharedPreferences.getString("monthSelection", "none")
    }
}
