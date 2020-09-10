package com.money.budget.wealthy.ui.expenses.manageexpenses

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.database.models.CategoryTypesEntity
import com.money.budget.wealthy.database.models.TransactionTypesEntity
import com.money.budget.wealthy.databinding.AddExpensesFragmentBinding
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddExpenseFragment : Fragment(R.layout.add_expenses_fragment) {

    private val binding by viewBinding(AddExpensesFragmentBinding::bind)

    private val viewModel: ManageExpenseViewModel by viewModel()

    private val transactionTypeAdapter: TransactionTypesAdapter by lazy {
        TransactionTypesAdapter { onTransactionTypePicked(it) }
    }

    private fun onTransactionTypePicked(transactionTypes: TransactionTypesEntity) {
        viewModel.setTransactionTypes(transactionTypes)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupClickListener()
        setupObservers()
        setupLists()

        viewModel.loadTransactionTypes()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Add transaction"
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListener() {
        binding.apply {
            accountType.parent.debouncedClick(lifecycleScope) {
                viewModel.chooseAccount()
            }
            category.parent.debouncedClick(lifecycleScope) {
                viewModel.chooseCategory()
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is ManageExpenseUIState.Loading -> { }
                is ManageExpenseUIState.Success -> { }
                is ManageExpenseUIState.Accounts -> renderAccounts(it.accountsEntity)
                is ManageExpenseUIState.CategoryTypes -> renderCategory(it.categoryTypesEntity)
                is ManageExpenseUIState.TransactionTypes -> renderTransactionTypes(it.transactionTypesEntity)
                is ManageExpenseUIState.Error -> { }
            }
        }

        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is ManageExpenseActions.Navigate -> findNavController().navigate(it.destination)
                is ManageExpenseActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun renderAccounts(accounts: AccountsEntity) {
        binding.apply {
            accountType.apply {
                accountTypeName.text = accounts.sourceName
                accountTypeDescription.text = accounts.sourceDescription
                accountTypeDescription.isVisible = true
            }
        }
    }

    private fun renderCategory(categoryType: CategoryTypesEntity) {
        binding.apply {
            category.apply {
                accountTypeName.text = categoryType.categoryName
                accountTypeDescription.text = categoryType.categoryDescription
                accountTypeDescription.isVisible = true
            }
        }
    }

    private fun renderTransactionTypes(transactionTypes: List<TransactionTypesEntity>) {
        transactionTypeAdapter.setTransactionTypes(transactionType = transactionTypes)
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun setupLists() {
        binding.apply {
            transactionTypes.adapter = transactionTypeAdapter
        }
    }
}
