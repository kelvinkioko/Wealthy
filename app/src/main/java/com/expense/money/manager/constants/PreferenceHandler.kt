package com.expense.money.manager.constants

import android.content.Context
import android.content.SharedPreferences

class PreferenceHandler(activity: Context) {

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

    fun setCurrency(currencySelection: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("currency", currencySelection)
        editor.apply()
    }

    fun getCurrency(): String? {
        return sharedPreferences.getString("currency", "USD")
    }

    /*
     * Project set up
     */
    fun setCurrencySetup(currencySetup: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("currencySetup", currencySetup)
        editor.apply()
    }

    fun getCurrencySetup(): Boolean? {
        return sharedPreferences.getBoolean("currencySetup", false)
    }

    fun setAccountTypesSetup(accountTypesSetup: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("accountTypesSetup", accountTypesSetup)
        editor.apply()
    }

    fun getAccountTypesSetup(): Boolean? {
        return sharedPreferences.getBoolean("accountTypesSetup", false)
    }

    fun setAccountSetup(accountSetup: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("accountSetup", accountSetup)
        editor.apply()
    }

    fun getAccountSetup(): Boolean? {
        return sharedPreferences.getBoolean("accountSetup", false)
    }

    fun setExpensesSetup(expensesSetup: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("expensesSetup", expensesSetup)
        editor.apply()
    }

    fun getExpensesSetup(): Boolean? {
        return sharedPreferences.getBoolean("expensesSetup", false)
    }
}
