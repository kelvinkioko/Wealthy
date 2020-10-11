package com.expense.money.manager.ui.expenses.manageexpenses.choose

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.expense.money.manager.R
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.databinding.ChooseCategoryDialogBinding
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseCategoryDialogFragment(private val categories: List<CategoryTypesEntity>, val resendCategoryCallback: (CategoryTypesEntity) -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(ChooseCategoryDialogBinding::bind)

    private val chooseCategoryAdapter: ChooseCategoryAdapter by lazy {
        ChooseCategoryAdapter { onAccountPicked(it) }
    }

    private fun onAccountPicked(category: CategoryTypesEntity) {
        resendCategoryCallback(category)
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
    ) = inflater.inflate(R.layout.choose_category_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupLists()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Choose category"
            toolbar.setNavigationOnClickListener { dismiss() }
        }
    }

    private fun setupLists() {
        binding.apply {
            chooseCategoryAdapter.setCategories(categories = categories)
            category.adapter = chooseCategoryAdapter
        }
    }
}
