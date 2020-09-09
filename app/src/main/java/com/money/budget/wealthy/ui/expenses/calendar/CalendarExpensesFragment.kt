package com.money.budget.wealthy.ui.expenses.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesCalendarFragmentBinding
import com.money.budget.wealthy.util.viewBinding

class CalendarExpensesFragment : Fragment(R.layout.expenses_calendar_fragment) {

    private val binding by viewBinding(ExpensesCalendarFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
