package com.money.budget.wealthy.constants

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

class Hive {

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("dd-MM-yyyy")
        return mdformat.format(calendar.time)
    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return mdformat.format(calendar.time)
    }

    fun getTimestamp(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("yyyyMMdd_HHmmss")
        return mdformat.format(calendar.time)
    }
}
