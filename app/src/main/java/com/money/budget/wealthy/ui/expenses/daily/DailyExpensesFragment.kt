package com.money.budget.wealthy.ui.expenses.daily

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesDailyFragmentBinding
import com.money.budget.wealthy.util.viewBinding

class DailyExpensesFragment : Fragment(R.layout.expenses_daily_fragment) {

    private val binding by viewBinding(ExpensesDailyFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
