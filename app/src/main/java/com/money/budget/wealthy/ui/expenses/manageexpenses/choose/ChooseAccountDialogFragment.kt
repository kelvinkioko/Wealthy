package com.money.budget.wealthy.ui.expenses.manageexpenses.choose

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.databinding.ChooseAccountDialogBinding
import com.money.budget.wealthy.util.viewBinding

class ChooseAccountDialogFragment(private val accounts: List<AccountsEntity>, val resendAccountCallback: (AccountsEntity) -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(ChooseAccountDialogBinding::bind)

    private val chooseAccountAdapter: ChooseAccountAdapter by lazy {
        ChooseAccountAdapter { onAccountPicked(it) }
    }

    private fun onAccountPicked(account: AccountsEntity) {
        resendAccountCallback(account)
        dismiss()
    }

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
    ) = inflater.inflate(R.layout.choose_account_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupLists()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Choose account"
            toolbar.setNavigationOnClickListener { dismiss() }
        }
    }

    private fun setupLists() {
        binding.apply {
            chooseAccountAdapter.setAccounts(accounts = accounts)
            account.adapter = chooseAccountAdapter
        }
    }
}
