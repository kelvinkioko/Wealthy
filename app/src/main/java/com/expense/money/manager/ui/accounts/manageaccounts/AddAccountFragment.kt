package com.expense.money.manager.ui.accounts.manageaccounts

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
import androidx.navigation.fragment.navArgs
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.database.models.AccountsEntity
import com.expense.money.manager.databinding.AccountAddFragmentBinding
import com.expense.money.manager.ui.accounts.AccountsActions
import com.expense.money.manager.ui.accounts.AccountsUIState
import com.expense.money.manager.ui.accounts.AccountsViewModel
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAccountFragment : Fragment(R.layout.account_add_fragment) {

    private val binding by viewBinding(AccountAddFragmentBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()
    private val args: AddAccountFragmentArgs by navArgs()

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
                is AccountsUIState.Account -> populateEditContent(it.accountEntity)
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

    private fun populateEditContent(accountEntity: AccountsEntity) {
        binding.apply {
            accountName.editText!!.setText(accountEntity.sourceName)
            accountAmount.editText!!.setText(accountEntity.sourceBalance)
            accountAmount.editText!!.isEnabled = false
            accountNumber.editText!!.setText(accountEntity.sourceNumber)
            accountType.accountTypeName.text.toString()
            accountDescription.editText!!.setText(accountEntity.sourceDescription)
        }
    }

    private fun renderAccountTypeCallBack(accountTypesEntity: AccountTypesEntity) {
        binding.apply {
            accountType.apply {
                accountTypeName.text = accountTypesEntity.accountTypeName
                accountTypeDescription.text = accountTypesEntity.accountTypeDescription
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
                            sourceDescription = accountDescription.editText!!.text.toString(),
                            sourceStatus = StatusEnum.ACTIVE,
                            createdAt = Hive().getCurrentDateTime()
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

            if (args.accountID.isNotEmpty()) {
                viewModel.loadAccountByID(args.accountID)
            }
        }
    }
}
