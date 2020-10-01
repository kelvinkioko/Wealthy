package com.expense.money.manager.ui.settings.expensecategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expense.money.manager.database.repository.CategoryRepository

class ExpenseCategoryViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<ExpenseCategoryUIState>()
    val uiState: LiveData<ExpenseCategoryUIState> = _uiState

    init {
        loadExpenseCategory()
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
}

sealed class ExpenseCategoryUIState {
    object Loading : ExpenseCategoryUIState()

    object Success : ExpenseCategoryUIState()

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
