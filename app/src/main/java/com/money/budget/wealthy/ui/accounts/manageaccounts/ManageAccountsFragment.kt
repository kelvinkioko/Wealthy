package com.money.budget.wealthy.ui.accounts.manageaccounts

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.AccountManageFragmentBinding
import com.money.budget.wealthy.ui.accounts.AccountsActions
import com.money.budget.wealthy.ui.accounts.AccountsUIState
import com.money.budget.wealthy.ui.accounts.AccountsViewModel
import com.money.budget.wealthy.ui.accounts.SectionedAccountItem
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ManageAccountsFragment : Fragment(R.layout.account_manage_fragment) {

    private val binding by viewBinding(AccountManageFragmentBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    private val accountAdapter: ManageAccountsAdapter by lazy { ManageAccountsAdapter() }

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
                is AccountsUIState.ManageAccounts -> populateAccounts(it.accountEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountsActions.Navigate -> findNavController().navigate(it.destination)
                is AccountsActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun populateAccounts(accountsEntity: List<SectionedAccountItem>) {
        if (accountsEntity.isEmpty()) {
            binding.apply {
                // Show empty
                emptyState.emptyParent.isVisible = true
                emptyState.emptyText.text = String.format(getString(R.string.there_is_no_list_of_accounts), "accounts")
                emptyState.emptyAction.text = String.format(getString(R.string.add_an_account), "an account")
                // Hide recycler
                accountsList.isGone = true
            }
        } else {
            binding.apply {
                // Hide empty
                emptyState.emptyParent.isGone = true
                // Show recycler
                accountsList.isVisible = true
            }
            accountAdapter.submitList(accountsEntity)
        }
    }

    private fun setupAccountTypesList() {
        binding.accountsList.adapter = accountAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadManageAccounts()
    }
}
