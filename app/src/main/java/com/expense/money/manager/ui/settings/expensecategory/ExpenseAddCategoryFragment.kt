package com.expense.money.manager.ui.settings.expensecategory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.expense.money.manager.R
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.databinding.SettingsExpenseAddFragmentBinding
import com.expense.money.manager.ui.expenses.manageexpenses.TransactionTypesAdapter
import com.expense.money.manager.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExpenseAddCategoryFragment : Fragment(R.layout.settings_expense_add_fragment) {

    private val binding by viewBinding(SettingsExpenseAddFragmentBinding::bind)

    private val viewModel: ExpenseCategoryViewModel by viewModel()
    private val args: ExpenseAddCategoryFragmentArgs by navArgs()

    private lateinit var categoryTypesEntity: CategoryTypesEntity
    private lateinit var transactionTypesEntity: TransactionTypesEntity

    private val transactionTypeAdapter: TransactionTypesAdapter by lazy {
        TransactionTypesAdapter { onTransactionTypePicked(it) }
    }

    private fun onTransactionTypePicked(transactionTypesEntity: TransactionTypesEntity) {
        this.transactionTypesEntity = transactionTypesEntity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupLists()
        setupClickListeners()
        setupViewObservers()
        setupData()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupLists() {
        binding.apply {
            transactionTypesList.adapter = transactionTypeAdapter
        }
    }

    private fun setupClickListeners() {
        binding.apply {
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
                is ExpenseCategoryUIState.Success -> { findNavController().navigateUp() }
                is ExpenseCategoryUIState.TransactionTypesByID -> {
                    this.transactionTypesEntity = it.transactionTypesEntity
                    viewModel.loadTransactionTypes()
                }
                is ExpenseCategoryUIState.TransactionTypes -> { renderTransactionType(it.transactionTypesEntity) }
                is ExpenseCategoryUIState.Category -> { renderExpenseCategoryDetails(it.categoryEntity) }
            }
        }
    }

    private fun renderExpenseCategoryDetails(categoryTypesEntity: CategoryTypesEntity) {
        this.categoryTypesEntity = categoryTypesEntity
        binding.apply {
            expenseName.editText!!.setText(categoryTypesEntity.categoryName)
            expenseDescription.editText!!.setText(categoryTypesEntity.categoryDescription)
        }
        val transactionID = categoryTypesEntity.transactionType.split("#")[1]
        viewModel.loadTransactionTypeByID(transactionID = transactionID)
    }

    private fun renderTransactionType(transactionTypesEntity: List<TransactionTypesEntity>) {
        val selected = if (::categoryTypesEntity.isInitialized) { categoryTypesEntity.transactionType.split("#")[0] } else { "" }
        transactionTypeAdapter.setTransactionTypes(transactionType = transactionTypesEntity, selected = selected)
    }

    private fun setupData() {
        binding.apply {
            if (args.categoryID.isNotEmpty()) {
                viewModel.loadCategoryTypeByID(categoryID = args.categoryID)
            } else {
                viewModel.loadTransactionTypes()
            }
        }
    }
}
