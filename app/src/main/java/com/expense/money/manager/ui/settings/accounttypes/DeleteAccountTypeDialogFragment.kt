package com.expense.money.manager.ui.settings.accounttypes

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.expense.money.manager.R
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.databinding.SettingsAccountTypeDeleteDialogBinding
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteAccountTypeDialogFragment(private val accountTypeID: String, val deleteCallBack: () -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(SettingsAccountTypeDeleteDialogBinding::bind)

    private val viewModel: AccountTypeViewModel by viewModel()
    private lateinit var accountTypesEntity: AccountTypesEntity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val params = bottomSheet.layoutParams
            bottomSheet.layoutParams = params
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.settings_account_type_delete_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupObservers()
        setupClickListeners()

        if (accountTypeID.isNotEmpty()) {
            viewModel.loadAccountTypeByID(accountTypeID = accountTypeID)
        }
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Delete/Archive account"
            toolbar.setNavigationOnClickListener { dismiss() }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AccountTypeUIState.Success -> {
                    dismiss()
                }
                is AccountTypeUIState.AccountTypeDetails -> {
                    setupData(it.accountType)
                }
            }
        }
    }

    private fun setupData(accountTypesEntity: AccountTypesEntity) {
        this.accountTypesEntity = accountTypesEntity
        binding.apply {
            deleteMessage.text = String.format(getString(R.string.delete_account_type_warning), accountTypesEntity.accountTypeName)
            archiveMessage.text = getString(R.string.archive_account_type_warning)
            delete.text = String.format(getString(R.string.delete_account_type), accountTypesEntity.accountTypeName)
            archive.text = String.format(getString(R.string.archive_account_type), accountTypesEntity.accountTypeName)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            delete.debouncedClick(lifecycleScope) {
                viewModel.deleteAccount(accountTypeID = accountTypesEntity.accountTypeID)
                deleteCallBack()
                dismiss()
            }

            archive.debouncedClick(lifecycleScope) {
                viewModel.archiveAccount(accountTypeID = accountTypesEntity.accountTypeID)
                deleteCallBack()
                dismiss()
            }
        }
    }
}
