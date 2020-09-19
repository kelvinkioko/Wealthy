package com.money.budget.wealthy.ui.expenses.weekly

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ExpensesWeeklyFragmentBinding
import com.money.budget.wealthy.ui.expenses.SharedExpenseViewModel
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeeklyExpensesFragment : Fragment(R.layout.expenses_weekly_fragment) {

    private val binding by viewBinding(ExpensesWeeklyFragmentBinding::bind)

    private val viewModel: WeeklyExpensesViewModel by viewModel()

    private val expensesAdapter: WeeklyExpensesAdapter by lazy { WeeklyExpensesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewObservers()
        setupAccountTypesList()

        val sharedViewModel = requireActivity().run { ViewModelProvider(this).get(SharedExpenseViewModel::class.java) }
        sharedViewModel.toolbarCalendar.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
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

    private fun populateExpenses(expensesEntity: List<WeeklyTransactionsEntity>) {
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
