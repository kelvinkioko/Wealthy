package com.expense.money.manager.ui.settings.accounttypes

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.databinding.SettingsAccountTypeAddDialogBinding
import com.expense.money.manager.ui.SharedViewModel
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddAccountTypeDialog(private val accountTypeID: String, val successCallBack: () -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(SettingsAccountTypeAddDialogBinding::bind)

    private val viewModel: AccountTypeViewModel by viewModel()

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var accountTypesEntity: AccountTypesEntity

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

        sharedViewModel = requireActivity().run { ViewModelProvider(this).get(SharedViewModel::class.java) }

        setupToolbar()
        setupObservers()
        setupClickListeners()
        setupData()
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
                is AccountTypeUIState.Success -> {
                    sharedViewModel.setReload(true)
                    successCallBack()
                    dismiss()
                }
                is AccountTypeUIState.AccountTypeDetails -> {
                    renderAccountTypeDetails(it.accountType)
                }
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is AccountTypeActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun renderAccountTypeDetails(accountTypesEntity: AccountTypesEntity) {
        this.accountTypesEntity = accountTypesEntity
        binding.apply {
            accountName.editText!!.setText(accountTypesEntity.accountTypeName)
            accountDescription.editText!!.setText(accountTypesEntity.accountDescription)
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
                        sourceID = if (::accountTypesEntity.isInitialized) { accountTypesEntity.sourceID } else { "acctyp-${Hive().getTimestamp()}" },
                        accountTypeName = accountName.editText!!.text.toString(),
                        accountDescription = accountDescription.editText!!.text.toString(),
                        createdAt = Hive().getCurrentDateTime()
                    )
                    viewModel.saveAccountType(accountType = accountType, update = ::accountTypesEntity.isInitialized)
                }
            }
        }
    }

    private fun setupData() {
        binding.apply {
            if (accountTypeID.isNotEmpty()) {
                viewModel.loadAccountTypeByID(accountTypeID = accountTypeID)
            }
        }
    }
}
