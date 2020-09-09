package com.money.budget.wealthy.ui.accounts.manage_accounts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.databinding.AccountAddFragmentBinding
import com.money.budget.wealthy.ui.accounts.AccountsActions
import com.money.budget.wealthy.ui.accounts.AccountsUIState
import com.money.budget.wealthy.ui.accounts.AccountsViewModel
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAccountFragment : Fragment(R.layout.account_add_fragment) {

    private val binding by viewBinding(AccountAddFragmentBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupObservers()
        setupClickListeners()
        setupData()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AccountsUIState.Success -> findNavController().navigateUp()
                is AccountsUIState.AccountCallBack -> renderAccountTypeCallBack(it.accountTypesEntity)
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountsActions.Navigate -> findNavController().navigate(it.destination)
                is AccountsActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
    }

    private fun renderAccountTypeCallBack(accountTypesEntity: AccountTypesEntity) {
        binding.apply {
            accountType.apply {
                accountTypeName.text = accountTypesEntity.accountTypeName
                accountTypeDescription.text = accountTypesEntity.accountDescription
                accountTypeDescription.isVisible = true
            }
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    @SuppressLint("NewApi")
    private fun setupClickListeners() {
        binding.apply {
            accountHelp.debouncedClick(lifecycleScope) {
                Toast.makeText(requireContext(), "Help clicked", Toast.LENGTH_LONG).show()
            }
            accountType.parent.debouncedClick(lifecycleScope) {
                viewModel.selectAccountType()
            }
            saveAccount.debouncedClick(lifecycleScope) {
                when {
                    accountName.editText!!.text.toString().isEmpty() -> {
                        accountName.error = "Required"
                    }
                    accountType.accountTypeName.text.toString().isEmpty() -> {
                        accountTypeError.isVisible = true
                        accountType.accountView.setBackgroundColor(resources.getColor(R.color.colorNegative, null))
                    }
                    else -> {
                        accountTypeError.isGone = true
                        accountType.accountView.setBackgroundColor(resources.getColor(R.color.hint_text, null))

                        val account = AccountsEntity(
                            id = 0,
                            sourceID = "acc-${Hive().getTimestamp()}",
                            identifier = "",
                            sourceName = accountName.editText!!.text.toString(),
                            sourceBalance = accountAmount.editText!!.text.toString(),
                            sourceNumber = accountNumber.editText!!.text.toString(),
                            sourceType = accountType.accountTypeName.text.toString(),
                            sourceDescription = accountDescription.editText!!.text.toString()
                        )
                        viewModel.saveAccounts(account = account)
                    }
                }
            }
        }
    }

    private fun setupData() {
        binding.apply {
            accountType.apply {
                accountTypeName.hint = "Select account type"
            }
        }
    }
}
