package com.money.budget.wealthy.constants

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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

    fun formatCurrency(amount: Int): String {
        val df = DecimalFormat("#,###,###,###,###.00")
        df.minimumFractionDigits = 2
        return df.format(amount)
    }

    fun formatDateHeader(date: String): String {
        val inFormat = SimpleDateFormat("dd/MM/yyyy")
        val input: Date = inFormat.parse(date)
        val weekDayFormat = SimpleDateFormat("EEEE").format(input)
        val dayFormat = SimpleDateFormat("dd").format(input)
        val monthFormat = SimpleDateFormat("MM").format(input)
        val yearFormat = SimpleDateFormat("yyyy").format(input)

        return "$weekDayFormat#$dayFormat#$monthFormat#$yearFormat"
    }
}
