package com.money.budget.wealthy.ui.settings.account_types

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.databinding.SettingsManageAccountTypesFragmentBinding
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountTypesFragment : Fragment(R.layout.settings_manage_account_types_fragment) {

    private val binding by viewBinding(SettingsManageAccountTypesFragmentBinding::bind)

    private val viewModel: AccountTypeViewModel by viewModel()

    private val accountTypesAdapter: AccountTypesAdapter by lazy {
        AccountTypesAdapter { onAccountTypePicked(it) }
    }

    private fun onAccountTypePicked(accountType: AccountTypesEntity) {
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

    private fun populateAccountTypes(accountTypesEntity: List<AccountTypesEntity>) {
        accountTypesAdapter.setAccountTypes(accountTypesEntity)
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
