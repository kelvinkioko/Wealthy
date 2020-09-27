package com.money.budget.wealthy.ui.accounts.accounttypes

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.databinding.AccountSelectTypeDialogBinding
import com.money.budget.wealthy.ui.SharedViewModel
import com.money.budget.wealthy.ui.accounts.AccountsUIState
import com.money.budget.wealthy.ui.accounts.AccountsViewModel
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeDialogFragment(
    val resendAccountTypeCallback: (AccountTypesEntity) -> (Unit),
    val addAccountCallback: () -> (Unit)
) : BottomSheetDialogFragment() {

    private val binding by viewBinding(AccountSelectTypeDialogBinding::bind)

    private val viewModel: AccountsViewModel by viewModel()

    private lateinit var sharedViewModel: SharedViewModel

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

        sharedViewModel = requireActivity().run { ViewModelProvider(this).get(SharedViewModel::class.java) }

        setupToolbar()
        setupObservers()
        setupList()
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
                is AccountsUIState.AccountTypeAdded -> {
                    viewModel.loadAccountTypes()
                    println("Updating the account was added")
                }
                is AccountsUIState.AccountTypes -> renderAccountTypes(it.accountTypesEntity)
            }
        }
        sharedViewModel.reload.observe(viewLifecycleOwner, Observer {
            if (it.toString().toBoolean()) {
                viewModel.loadAccountTypes()
            }
        })
    }

    private fun renderAccountTypes(accountType: List<AccountTypesEntity>) {
        if (accountType.isEmpty()) {
            binding.apply {
                // Show empty
                emptyState.emptyParent.isVisible = true
                emptyState.emptyText.text = String.format(getString(R.string.there_is_no_list_of_accounts), "account accountTypes")
                emptyState.emptyAction.text = String.format(getString(R.string.add_an_account), "account types")

                emptyState.emptyAction.setOnClickListener {
                    addAccountCallback()
                }
                // Hide recycler
                accountTypes.isGone = true
            }
        } else {
            binding.apply {
                // Hide empty
                emptyState.emptyParent.isGone = true
                // Show recycler
                accountTypes.isVisible = true
            }
            typeAdapter.setAccountTypes(accountType = accountType)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccountTypes()
    }
}
