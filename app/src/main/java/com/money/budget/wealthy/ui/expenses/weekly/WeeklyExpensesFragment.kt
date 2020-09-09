package com.money.budget.wealthy.ui.expenses.weekly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesWeeklyFragmentBinding
import com.money.budget.wealthy.util.viewBinding

class WeeklyExpensesFragment : Fragment(R.layout.expenses_weekly_fragment) {

    private val binding by viewBinding(ExpensesWeeklyFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
