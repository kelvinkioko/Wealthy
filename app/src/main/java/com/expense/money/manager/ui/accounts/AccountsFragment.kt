package com.expense.money.manager.ui.accounts

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.database.models.AccountsEntity
import com.expense.money.manager.databinding.AccountsFragmentBinding
import com.expense.money.manager.ui.accounts.adapters.AccountsAdapter
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountsFragment : Fragment(R.layout.accounts_fragment) {

    private val binding by viewBinding(AccountsFragmentBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    private val accountAdapter: AccountsAdapter by lazy {
        AccountsAdapter { onAccountPicked(it) }
    }

    private fun onAccountPicked(accountEntity: SectionedAccountDetailsItem.AccountsEntity) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupViewObservers()
        setupAccountTypesList()
    }

    private fun setupClickListeners() {
        binding.apply {
            manageAccounts.debouncedClick(lifecycleScope) {
                viewModel.onManageClicked()
            }
            emptyState.emptyAction.debouncedClick(lifecycleScope) {
                viewModel.onManageClicked()
            }
        }
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AccountsUIState.Accounts -> populateAccounts(it.accountEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountsActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun populateAccounts(accountsEntity: List<SectionedAccountDetailsItem>) {
        if (accountsEntity.isEmpty()) {
            binding.apply {
                // Show empty
                emptyState.emptyParent.isVisible = true
                emptyState.emptyText.text = String.format(getString(R.string.there_is_no_list_of_accounts), "accounts")
                emptyState.emptyAction.text = String.format(getString(R.string.add_an_account), "an account")
                // Hide recycler
                groupList.isGone = true
                // Rename some buttons
                manageAccounts.text = getString(R.string.add_account)
            }
        } else {
            binding.apply {
                // Hide empty
                emptyState.emptyParent.isGone = true
                // Show recycler
                groupList.isVisible = true
                // Rename some buttons
                manageAccounts.text = getString(R.string.manage_account)
            }
            accountAdapter.submitList(accountsEntity)
        }
    }

    private fun setupAccountTypesList() {
        binding.groupList.adapter = accountAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }
}
