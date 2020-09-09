package com.money.budget.wealthy.ui.settings.account_types

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.databinding.SettingsAccountTypeAddDialogBinding
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAccountTypeDialog(private val accountID: String) : BottomSheetDialogFragment() {

    private val binding by viewBinding(SettingsAccountTypeAddDialogBinding::bind)

    private val viewModel: AccountTypeViewModel by viewModel()

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
    ) = inflater.inflate(R.layout.settings_account_type_add_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupObservers()
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Create account type"
            toolbar.setNavigationOnClickListener { dismiss() }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is AccountTypeUIState.Success -> dismiss()
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountTypeActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            saveAccountType.debouncedClick(lifecycleScope) {
                if (accountName.editText!!.text.toString().isEmpty()) {
                    accountName.error = "Required"
                } else {
                    val accountType = AccountTypesEntity(
                        id = 0,
                        sourceID = "acctyp-${Hive().getTimestamp()}",
                        accountTypeName = accountName.editText!!.text.toString(),
                        accountDescription = accountDescription.editText!!.text.toString(),
                        createdAt = Hive().getCurrentDateTime()
                    )
                    viewModel.saveAccountType(accountType = accountType)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireContext(), accountID, Toast.LENGTH_LONG).show()
    }
}
