package com.expense.money.manager.database.repository

import android.app.Application
import com.expense.money.manager.database.WealthyDatabase
import com.expense.money.manager.database.models.CategoryTypesEntity

class CategoryRepository(application: Application) {

    private val db = WealthyDatabase.getDatabase(application)
    private val categoryTypesDao = db.categoryTypesDao()

    fun insertCategoryTypes(transactionTypesEntity: CategoryTypesEntity) {
        categoryTypesDao.insertCategoryTypes(transactionTypesEntity)
    }

    fun loadCategoryTypes(): List<CategoryTypesEntity> {
        return categoryTypesDao.loadCategoryTypes()
    }

    fun loadCategoryTypes(transactionType: String): List<CategoryTypesEntity> {
        return categoryTypesDao.loadCategoryTypes(transactionType)
    }

    fun countCategoryTypes(): Int {
        return categoryTypesDao.countCategoryTypes()
    }

    fun deleteCategoryTypes() {
        categoryTypesDao.deleteCategoryTypes()
    }
}
