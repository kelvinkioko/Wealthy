package com.money.budget.wealthy.ui.expenses.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesCalendarFragmentBinding
import com.money.budget.wealthy.util.viewBinding
import com.money.budget.wealthy.widget.calendar.views.EventsCalendar
import java.util.Calendar

class CalendarExpensesFragment : Fragment(R.layout.expenses_calendar_fragment), EventsCalendar.Callback {

    private val binding by viewBinding(ExpensesCalendarFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendar()
    }

    private fun setupCalendar() {
        val today = Calendar.getInstance()

        val start = Calendar.getInstance()
        start.add(Calendar.YEAR, -10)

        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 20)

        binding.apply {
            eventsCalendar.setSelectionMode(eventsCalendar.SINGLE_SELECTION)
                .setToday(today)
                .setMonthRange(start, end)
                .setWeekStartDay(Calendar.SUNDAY, false)
                .setCallback(this@CalendarExpensesFragment)
                .build()

            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_MONTH, 2)
            eventsCalendar.addEvent(c)
            c.add(Calendar.DAY_OF_MONTH, 3)
            eventsCalendar.addEvent(c)
            c.add(Calendar.DAY_OF_MONTH, 4)
            eventsCalendar.addEvent(c)
            c.add(Calendar.DAY_OF_MONTH, 7)
            eventsCalendar.addEvent(c)
            c.add(Calendar.MONTH, 1)
            c[Calendar.DAY_OF_MONTH] = 1
            eventsCalendar.addEvent(c)

            val dc = Calendar.getInstance()
            dc.add(Calendar.DAY_OF_MONTH, 2)
        }
    }

    override fun onDaySelected(selectedDate: Calendar?) {
        binding.eventsCalendar.setCurrentSelectedDate(selectedDate!!)
        Snackbar.make(binding.root, getDateString(selectedDate?.timeInMillis), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDayLongPressed(selectedDate: Calendar?) {}

    override fun onMonthChanged(monthStartDate: Calendar?) {}

    private fun getDateString(time: Long?): String {
        if (time != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = time
            val month = when (cal[Calendar.MONTH]) {
                Calendar.JANUARY -> "January"
                Calendar.FEBRUARY -> "February"
                Calendar.MARCH -> "March"
                Calendar.APRIL -> "April"
                Calendar.MAY -> "May"
                Calendar.JUNE -> "June"
                Calendar.JULY -> "July"
                Calendar.AUGUST -> "August"
                Calendar.SEPTEMBER -> "September"
                Calendar.OCTOBER -> "October"
                Calendar.NOVEMBER -> "November"
                Calendar.DECEMBER -> "December"
                else -> ""
            }
            return "$month ${cal[Calendar.DAY_OF_MONTH]}, ${cal[Calendar.YEAR]}"
        } else return ""
    }
}
