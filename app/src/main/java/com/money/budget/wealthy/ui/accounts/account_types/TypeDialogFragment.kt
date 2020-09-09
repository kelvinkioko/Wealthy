package com.money.budget.wealthy.ui.accounts.account_types

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.databinding.AccountSelectTypeDialogBinding
import com.money.budget.wealthy.ui.accounts.AccountsUIState
import com.money.budget.wealthy.ui.accounts.AccountsViewModel
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeDialogFragment(val resendAccountTypeCallback: (AccountTypesEntity) -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(AccountSelectTypeDialogBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    private val typeAdapter: TypeAdapter by lazy {
        TypeAdapter { onTypePicked(it) }
    }

    private fun onTypePicked(accountType: AccountTypesEntity) {
        resendAccountTypeCallback(accountType)
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val params = bottomSheet.layoutParams
            params.height = (Resources.getSystem().displayMetrics.heightPixels)
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
    ) = inflater.inflate(R.layout.account_select_type_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupObservers()
        setupList()

        viewModel.loadAccountTypes()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Create account type"
            toolbar.setNavigationOnClickListener { dismiss() }
        }
    }

    private fun setupList() {
        binding.accountTypes.adapter = typeAdapter
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AccountsUIState.Success -> dismiss()
                is AccountsUIState.AccountTypes -> renderAccountTypes(it.accountTypesEntity)
            }
        }
    }

    private fun renderAccountTypes(accountType: List<AccountTypesEntity>) {
        typeAdapter.setAccountTypes(accountType = accountType)
    }
}
