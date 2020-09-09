package com.money.budget.wealthy.ui.accounts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.databinding.AccountsFragmentBinding
import com.money.budget.wealthy.ui.accounts.adapters.AccountsAdapter
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountsFragment : Fragment(R.layout.accounts_fragment) {

    private val binding by viewBinding(AccountsFragmentBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    private val accountAdapter: AccountsAdapter by lazy {
        AccountsAdapter { onAccountPicked(it) }
    }

    private fun onAccountPicked(account: AccountsEntity) {
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
                findNavController().navigate(AccountsFragmentDirections.toManageAccountFragment())
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
        binding.groupList.adapter = accountAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }
}
