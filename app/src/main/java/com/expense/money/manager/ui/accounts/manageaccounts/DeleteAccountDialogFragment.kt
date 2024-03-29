package com.expense.money.manager.ui.accounts.manageaccounts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.AccountDeleteDialogBinding
import com.expense.money.manager.ui.accounts.AccountsActions
import com.expense.money.manager.ui.accounts.AccountsUIState
import com.expense.money.manager.ui.accounts.AccountsViewModel
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteAccountDialogFragment(private val accountID: String, val deleteCallBack: () -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(AccountDeleteDialogBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

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
    ) = inflater.inflate(R.layout.account_delete_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupObservers()
        setupClickListeners()
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
                is AccountsUIState.Success -> dismiss()
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountsActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            delete.debouncedClick(lifecycleScope) {
                viewModel.deleteAccount(accountID)
                deleteCallBack()
                dismiss()
            }

            archive.debouncedClick(lifecycleScope) {
                viewModel.archiveAccount(accountID)
                deleteCallBack()
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
