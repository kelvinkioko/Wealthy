package com.money.budget.wealthy.ui.expenses.daily

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesDailyFragmentBinding
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DailyExpensesFragment : Fragment(R.layout.expenses_daily_fragment) {

    private val binding by viewBinding(ExpensesDailyFragmentBinding::bind)

    private val viewModel: DailyExpensesViewModel by viewModel()

    private val expensesAdapter: DailyExpensesAdapter by lazy { DailyExpensesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewObservers()
        setupAccountTypesList()
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is DailyExpensesUIState.DailyExpenses -> populateExpenses(it.expensesEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is DailyExpensesActions.Navigate -> findNavController().navigate(it.destination)
                is DailyExpensesActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun populateExpenses(expensesEntity: List<DisplayTransactionsEntity>) {
        expensesAdapter.setExpenses(expensesEntity)
    }

    private fun setupAccountTypesList() {
        binding.expensesList.adapter = expensesAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadExpenses()
    }
}
