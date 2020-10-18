package com.expense.money.manager.ui.settings.expensecategory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.SettingsExpenseCategoryFragmentBinding
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExpenseCategoryFragment : Fragment(R.layout.settings_expense_category_fragment) {

    private val binding by viewBinding(SettingsExpenseCategoryFragmentBinding::bind)

    private val viewModel: ExpenseCategoryViewModel by viewModel()

    private val expenseCurrencyAdapter: ExpenseCategoryAdapter by lazy {
        ExpenseCategoryAdapter { onCurrencyTypePicked(it) }
    }

    private fun onCurrencyTypePicked(categoryItems: SectionedCategoryItem.CategoryItems) {
        Toast.makeText(requireContext(), categoryItems.categoryName, Toast.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupClickListeners()
        setupAccountTypesList()
        setupViewObservers()
    }

    private fun setupClickListeners() {
        binding.apply {
            addCategoryTypes.setOnClickListener {
                viewModel.addOrEditCategoryTypes()
            }
        }
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is ExpenseCategoryUIState.Categories -> populateExpenseTypes(it.categoryEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is ExpenseCategoryActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun populateExpenseTypes(sectionedCurrencyItem: List<SectionedCategoryItem>) {
        expenseCurrencyAdapter.submitList(sectionedCurrencyItem)
    }

    private fun setupAccountTypesList() {
        binding.expenseCategoryList.adapter = expenseCurrencyAdapter
    }
}
