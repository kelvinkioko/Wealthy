package com.money.budget.wealthy.constants

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Hive {

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("dd-MM-yyyy")
        return mdformat.format(calendar.time)
    }

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("MMM, yyyy")
        return mdformat.format(calendar.time)
    }

    fun getPreviousMonth(currentMonth: String): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val currentDateFormat = SimpleDateFormat("MMM, yyyy")
        val date: Date = currentDateFormat.parse(currentMonth)!!
        calendar.time = date
        calendar.add(Calendar.MONTH, -1)
        return currentDateFormat.format(calendar.time)
    }

    fun getNextMonth(currentMonth: String): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val currentDateFormat = SimpleDateFormat("MMM, yyyy")
        val date: Date = currentDateFormat.parse(currentMonth)!!
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        return currentDateFormat.format(calendar.time)
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

    fun formatCurrency(amount: Float): String {
        val df = DecimalFormat("#,###,###,###,###.00")
        df.minimumFractionDigits = 2
        return df.format(amount)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateHeader(date: String): String {
        val inFormat = SimpleDateFormat("dd/MM/yyyy")
        val input: Date = inFormat.parse(date)
        val weekDayFormat = SimpleDateFormat("EEEE").format(input)
        val dayFormat = SimpleDateFormat("dd").format(input)
        val monthFormat = SimpleDateFormat("MM").format(input)
        val yearFormat = SimpleDateFormat("yyyy").format(input)

        return "$weekDayFormat#$dayFormat#$monthFormat#$yearFormat"
    }

    fun loadWeeks() {
        val cal = Calendar.getInstance()
        for (i in 0..11) {
            cal[Calendar.YEAR] = 2020
            cal[Calendar.DAY_OF_MONTH] = 1
            cal[Calendar.MONTH] = i
            val maxWeekNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH)
            println("loadWeeks For Month :: $i Num Week :: $maxWeekNumber")
        }
    }

    fun getWeekRange(year: Int, week_no: Int): Pair<String?, String?>? {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        cal[Calendar.YEAR] = year
        cal[Calendar.WEEK_OF_YEAR] = week_no
        val sunday = cal.time
        cal.add(Calendar.DATE, 6)
        val saturday = cal.time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        println("getWeekRange :: ${sdf.format(sunday)}, ${sdf.format(saturday)}")
        return Pair<String, String>(sdf.format(sunday), sdf.format(saturday))
    }

    fun getWeeksOfMonth(): ArrayList<String> {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = 2020
        cal[Calendar.MONTH] = 9 - 1
        cal[Calendar.DAY_OF_MONTH] = 1
        val monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val positionOfWeekOfYear: ArrayList<String> = ArrayList()
        for (i in 0 until monthDays) {
            cal.set(2020, 9 - 1, i)
            val weekOfYear: Int = cal.get(Calendar.WEEK_OF_YEAR)

            if (!positionOfWeekOfYear.contains("$weekOfYear")) {
                positionOfWeekOfYear.add("$weekOfYear")
                println("getWeeksOfYear :: $weekOfYear")
            }
            getWeekRange(2020, cal[Calendar.WEEK_OF_YEAR])
            cal.add(Calendar.DATE, 1)
        }
        return positionOfWeekOfYear
    }
}
