package com.expense.money.manager.ui.settings.expensecategory

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.observe
import com.expense.money.manager.R
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.databinding.SettingsAccountTypeDeleteDialogBinding
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteExpenseCategoryDialogFragment(private val categoryID: String, val deleteCallBack: () -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(SettingsAccountTypeDeleteDialogBinding::bind)

    private val viewModel: ExpenseCategoryViewModel by viewModel()
    private lateinit var categoryTypesEntity: CategoryTypesEntity

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

        if (categoryID.isNotEmpty()) {
            viewModel.loadCategoryTypeByID(categoryID = categoryID)
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
                is ExpenseCategoryUIState.Success -> { dismiss() }
                is ExpenseCategoryUIState.Category -> { setupData(it.categoryEntity) }
            }
        }
    }

    private fun setupData(categoryTypesEntity: CategoryTypesEntity) {
        this.categoryTypesEntity = categoryTypesEntity
        binding.apply {
            deleteMessage.text = String.format(getString(R.string.delete_account_type_warning), categoryTypesEntity.categoryName)
            archiveMessage.text = getString(R.string.archive_account_type_warning)
            delete.text = String.format(getString(R.string.delete_account_type), categoryTypesEntity.categoryName)
            archive.text = String.format(getString(R.string.archive_account_type), categoryTypesEntity.categoryName)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            delete.setOnClickListener {
                viewModel.deleteCategoryTypes(categoryID = categoryTypesEntity.categoryID)
                deleteCallBack()
                dismiss()
            }

            archive.setOnClickListener {
                viewModel.archiveCategoryTypes(categoryID = categoryTypesEntity.categoryID)
                deleteCallBack()
                dismiss()
            }
        }
    }
}
