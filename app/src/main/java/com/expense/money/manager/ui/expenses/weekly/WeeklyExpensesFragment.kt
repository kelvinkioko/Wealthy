package com.expense.money.manager.ui.expenses.weekly

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.ExpensesWeeklyFragmentBinding
import com.expense.money.manager.ui.expenses.SharedExpenseViewModel
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeeklyExpensesFragment : Fragment(R.layout.expenses_weekly_fragment) {

    private val binding by viewBinding(ExpensesWeeklyFragmentBinding::bind)

    private val viewModel: WeeklyExpensesViewModel by viewModel()

    private val expensesAdapter: WeeklyExpensesAdapter by lazy {
        WeeklyExpensesAdapter { onTransactionPicked(it) }
    }

    private fun onTransactionPicked(transactionsEntity: SectionedTransactionsEntity.DisplayTransactionsEntity) {
        Toast.makeText(requireContext(), transactionsEntity.expenseName, Toast.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewObservers()
        setupAccountTypesList()

        val sharedViewModel = requireActivity().run { ViewModelProvider(this).get(SharedExpenseViewModel::class.java) }
        sharedViewModel.toolbarMonthCalendar.observe(viewLifecycleOwner, Observer {
            binding.emptyState.emptyText.text = String.format(getString(R.string.empty_transactions_string), it.toString())
            viewModel.loadExpenses(it.toString())
        })
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is WeeklyExpensesUIState.WeeklyExpenses -> populateExpenses(it.expensesEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is WeeklyExpensesActions.Navigate -> findNavController().navigate(it.destination)
                is WeeklyExpensesActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun populateExpenses(expensesEntity: List<SectionedTransactionsEntity>) {
        if (expensesEntity.isEmpty()) {
            binding.apply {
                // Show empty
                emptyState.emptyParent.isVisible = true
                // Hide recycler
                expensesList.isGone = true
            }
        } else {
            binding.apply {
                // Hide empty
                emptyState.emptyParent.isGone = true
                // Show recycler
                expensesList.isVisible = true
            }
            expensesAdapter.submitList(expensesEntity)
        }
    }

    private fun setupAccountTypesList() {
        binding.expensesList.adapter = expensesAdapter
    }

    override fun onResume() {
        super.onResume()
    }
}
