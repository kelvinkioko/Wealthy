package com.money.budget.wealthy.ui.expenses.monthly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesMonthlyFragmentBinding
import com.money.budget.wealthy.util.viewBinding

class MonthlyExpensesFragment : Fragment(R.layout.expenses_monthly_fragment) {

    private val binding by viewBinding(ExpensesMonthlyFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
