package com.expense.money.manager.ui.settings.expensecategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.database.repository.CategoryRepository
import com.expense.money.manager.database.repository.TransactionRepository
import com.expense.money.manager.util.Event
import com.expense.money.manager.util.asEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ExpenseCategoryViewModel(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<ExpenseCategoryUIState>()
    val uiState: LiveData<ExpenseCategoryUIState> = _uiState

    private val _action = MutableLiveData<Event<ExpenseCategoryActions>>()
    val action: LiveData<Event<ExpenseCategoryActions>> = _action

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    init {
        loadExpenseCategory()
    }

    fun loadTransactionTypes() {
        val transactionTypes = transactionRepository.loadTransactionTypes()
        _uiState.postValue(ExpenseCategoryUIState.TransactionTypes(transactionTypes))
    }

    fun loadTransactionTypeByID(transactionID: String) {
        val transactionType = transactionRepository.loadTransactionType(transactionID = transactionID)
        _uiState.postValue(ExpenseCategoryUIState.TransactionTypesByID(transactionType))
    }

    fun addOrEditCategoryTypes(categoryID: String = "") {
        _action.postValue(ExpenseCategoryActions.Navigate(ExpenseCategoryFragmentDirections.toExpenseAddCategoryFragment(categoryID = categoryID)).asEvent())
    }

    fun loadExpenseCategory() {
        val currencies = categoryRepository.loadCategoryTypes()

        val sectionedCategoryItem = mutableListOf<SectionedCategoryItem>()
        var previousType = ""
        currencies.forEach {
            val currentType = it.transactionType
            if (previousType.isNullOrEmpty() || previousType != currentType) {
                previousType = currentType
                sectionedCategoryItem.add(SectionedCategoryItem.CategoryHeader(currentType))
            }
            sectionedCategoryItem.add(
                SectionedCategoryItem.CategoryItems(
                    categoryID = it.categoryID,
                    categoryName = it.categoryName,
                    categoryDescription = it.categoryDescription,
                    deleteCategoryClick = { deleteCategoryTypesSheet(it.categoryID) },
                    editCategoryClick = { addOrEditCategoryTypes(it.categoryID) }
                )
            )
        }

        _uiState.postValue(ExpenseCategoryUIState.Categories(sectionedCategoryItem))
    }

    fun loadCategoryTypeByID(categoryID: String) {
        val accountType = categoryRepository.loadCategoryTypeByID(categoryID = categoryID)
        _uiState.postValue(ExpenseCategoryUIState.Category(accountType))
    }

    fun saveCategoryType(categoryTypesEntity: CategoryTypesEntity, update: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                if (update) {
                    categoryRepository.updateCategoryTypes(
                        categoryID = categoryTypesEntity.categoryID,
                        categoryName = categoryTypesEntity.categoryName,
                        categoryDescription = categoryTypesEntity.categoryDescription,
                        transactionType = categoryTypesEntity.transactionType,
                        createdAt = categoryTypesEntity.createdAt
                    )
                } else {
                    categoryRepository.insertCategoryTypes(categoryTypesEntity = categoryTypesEntity)
                }
                _uiState.postValue(ExpenseCategoryUIState.Success)
            }
        }
    }

    private fun deleteCategoryTypesSheet(categoryID: String) {
        val bottomSheetFragment = DeleteExpenseCategoryDialogFragment(
            categoryID = categoryID,
            deleteCallBack = { loadExpenseCategory() }
        )
        _action.postValue(ExpenseCategoryActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun deleteCategoryTypes(categoryID: String) {
        categoryRepository.deleteOrArchiveCategoryTypesByID(categoryID = categoryID, categoryStatus = StatusEnum.DELETED)
    }

    fun archiveCategoryTypes(categoryID: String) {
        categoryRepository.deleteOrArchiveCategoryTypesByID(categoryID = categoryID, categoryStatus = StatusEnum.ARCHIVED)
    }
}

sealed class ExpenseCategoryActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : ExpenseCategoryActions()

    data class Navigate(val destination: NavDirections) : ExpenseCategoryActions()
}

sealed class ExpenseCategoryUIState {
    object Loading : ExpenseCategoryUIState()

    object Success : ExpenseCategoryUIState()

    data class TransactionTypesByID(val transactionTypesEntity: TransactionTypesEntity) : ExpenseCategoryUIState()

    data class TransactionTypes(val transactionTypesEntity: List<TransactionTypesEntity>) : ExpenseCategoryUIState()

    data class Category(val categoryEntity: CategoryTypesEntity) : ExpenseCategoryUIState()

    data class Categories(val categoryEntity: List<SectionedCategoryItem>) : ExpenseCategoryUIState()
}

sealed class SectionedCategoryItem {
    data class CategoryItems(
        val categoryID: String,
        val categoryName: String,
        val categoryDescription: String,
        var deleteCategoryClick: () -> Unit,
        var editCategoryClick: () -> Unit
    ) : SectionedCategoryItem()

    data class CategoryHeader(val title: String) : SectionedCategoryItem()
}
