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
import com.expense.money.manager.util.Event
import com.expense.money.manager.util.asEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ExpenseCategoryViewModel(
    private val categoryRepository: CategoryRepository
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

    fun addOrEditCategoryTypes(categoryID: String = "") {
        _action.postValue(ExpenseCategoryActions.Navigate(ExpenseCategoryFragmentDirections.toExpenseAddCategoryFragment(categoryID = categoryID)).asEvent())
    }

    private fun loadExpenseCategory() {
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
                    categoryDescription = it.categoryDescription
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

    fun deleteCategoryTypes(accountTypeID: String) {
        categoryRepository.deleteOrArchiveCategoryTypesByID(categoryID = accountTypeID, categoryStatus = StatusEnum.DELETED)
    }

    fun archiveCategoryTypes(accountTypeID: String) {
        categoryRepository.deleteOrArchiveCategoryTypesByID(categoryID = accountTypeID, categoryStatus = StatusEnum.ARCHIVED)
    }
}

sealed class ExpenseCategoryActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : ExpenseCategoryActions()

    data class Navigate(val destination: NavDirections) : ExpenseCategoryActions()
}

sealed class ExpenseCategoryUIState {
    object Loading : ExpenseCategoryUIState()

    object Success : ExpenseCategoryUIState()

    data class Category(val categoryEntity: CategoryTypesEntity) : ExpenseCategoryUIState()

    data class TransactionType(val transactionTypesEntity: TransactionTypesEntity) : ExpenseCategoryUIState()

    data class Categories(val categoryEntity: List<SectionedCategoryItem>) : ExpenseCategoryUIState()
}

sealed class SectionedCategoryItem {
    data class CategoryItems(
        val categoryID: String,
        val categoryName: String,
        val categoryDescription: String
    ) : SectionedCategoryItem()

    data class CategoryHeader(val title: String) : SectionedCategoryItem()
}
