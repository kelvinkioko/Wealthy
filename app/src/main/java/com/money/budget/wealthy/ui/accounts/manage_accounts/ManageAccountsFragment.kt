package com.money.budget.wealthy.ui.accounts.manage_accounts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.databinding.AccountManageFragmentBinding
import com.money.budget.wealthy.ui.accounts.AccountsActions
import com.money.budget.wealthy.ui.accounts.AccountsUIState
import com.money.budget.wealthy.ui.accounts.AccountsViewModel
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManageAccountsFragment : Fragment(R.layout.account_manage_fragment) {

    private val binding by viewBinding(AccountManageFragmentBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    private val accountAdapter: ManageAccountsAdapter by lazy {
        ManageAccountsAdapter { onAccountPicked(it) }
    }

    private fun onAccountPicked(account: AccountsEntity) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupClickListeners()
        setupViewObservers()
        setupAccountTypesList()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            addAccounts.debouncedClick(lifecycleScope) {
                viewModel.addOrEditAccounts("")
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

    private fun populateAccounts(accountsEntity: List<AccountsEntity>) {
        accountAdapter.setAccounts(accountsEntity)
    }

    private fun setupAccountTypesList() {
        binding.accountsList.adapter = accountAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }
}
