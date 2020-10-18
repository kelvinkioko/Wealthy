package com.expense.money.manager.ui.settings.expensecategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.databinding.SettingsExpenseAddFragmentBinding
import com.expense.money.manager.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExpenseAddCategoryFragment : Fragment(R.layout.settings_expense_add_fragment) {

    private val binding by viewBinding(SettingsExpenseAddFragmentBinding::bind)

    private val viewModel: ExpenseCategoryViewModel by viewModel()
    private lateinit var categoryTypesEntity: CategoryTypesEntity
    private lateinit var transactionTypesEntity: TransactionTypesEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupClickListeners()
        setupViewObservers()
        setupData()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            transactionType.parent.setOnClickListener {
            }
            saveExpenseType.setOnClickListener {
                if (expenseName.editText!!.text.toString().isEmpty()) {
                    expenseName.error = "Required"
                } else {
                    val categoryType = CategoryTypesEntity(
                        id = 0,
                        categoryID = if (::categoryTypesEntity.isInitialized) { categoryTypesEntity.categoryID } else { "CAT-${Hive().getTimestamp()}" },
                        categoryName = expenseName.editText!!.text.toString(),
                        categoryDescription = expenseDescription.editText!!.text.toString(),
                        transactionType = "${transactionTypesEntity.transactionName}#${transactionTypesEntity.transactionID}",
                        categoryStatus = StatusEnum.ACTIVE,
                        createdAt = Hive().getCurrentDateTime()
                    )
                    viewModel.saveCategoryType(categoryTypesEntity = categoryType, update = ::categoryTypesEntity.isInitialized)
                }
            }
        }
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is ExpenseCategoryUIState.Success -> { }
                is ExpenseCategoryUIState.Category -> { renderExpenseCategoryDetails(it.categoryEntity) }
                is ExpenseCategoryUIState.TransactionType -> { renderTransactionType(it.transactionTypesEntity) }
            }
        }
    }

    private fun renderExpenseCategoryDetails(categoryTypesEntity: CategoryTypesEntity) {
        this.categoryTypesEntity = categoryTypesEntity
        binding.apply {
            expenseName.editText!!.setText(categoryTypesEntity.categoryName)
            expenseDescription.editText!!.setText(categoryTypesEntity.categoryDescription)
        }
    }

    private fun renderTransactionType(transactionTypesEntity: TransactionTypesEntity) {
        this.transactionTypesEntity = transactionTypesEntity
        binding.transactionType.accountTypeName.text = transactionTypesEntity.transactionName
    }

    private fun setupData() {
        binding.apply {
            transactionType.apply {
                accountTypeName.text = "Select transaction category"
            }
//            if (accountTypeID.isNotEmpty()) {
//                viewModel.loadAccountTypeByID(accountTypeID = accountTypeID)
//            }
        }
    }
}
