package com.expense.money.manager.ui.settings.accounttypes

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.SettingsManageAccountTypesFragmentBinding
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountTypesFragment : Fragment(R.layout.settings_manage_account_types_fragment) {

    private val binding by viewBinding(SettingsManageAccountTypesFragmentBinding::bind)

    private val viewModel: AccountTypeViewModel by viewModel()

    private val accountTypesAdapter: AccountTypesAdapter by lazy {
        AccountTypesAdapter { onAccountTypePicked(it) }
    }

    private fun onAccountTypePicked(accountType: AccountTypesWithActionsEntity) {
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
            addAccountTypes.debouncedClick(lifecycleScope) {
                viewModel.addOrEditAccountType("")
            }
            emptyState.emptyAction.setOnClickListener {
                viewModel.addOrEditAccountType("")
            }
        }
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AccountTypeUIState.AccountTypes -> populateAccountTypes(it.accountTypesEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountTypeActions.Navigate -> findNavController().navigate(it.destination)
                is AccountTypeActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun populateAccountTypes(accountTypesEntity: List<AccountTypesWithActionsEntity>) {
        if (accountTypesEntity.isEmpty()) {
            binding.apply {
                // Show empty
                emptyState.emptyParent.isVisible = true
                emptyState.emptyText.text = String.format(getString(R.string.there_is_no_list_of_accounts), "account types")
                emptyState.emptyAction.text = String.format(getString(R.string.add_an_account), "an account type")
                // Hide recycler
                accountTypesList.isGone = true
            }
        } else {
            binding.apply {
                // Hide empty
                emptyState.emptyParent.isGone = true
                // Show recycler
                accountTypesList.isVisible = true
            }
            accountTypesAdapter.setAccountTypes(accountTypesEntity)
        }
    }

    private fun setupAccountTypesList() {
        binding.accountTypesList.adapter = accountTypesAdapter
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccountTypes()
    }
}
