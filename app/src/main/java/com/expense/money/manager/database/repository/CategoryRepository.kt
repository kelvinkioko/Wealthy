package com.expense.money.manager.database.repository

import android.app.Application
import com.expense.money.manager.database.WealthyDatabase
import com.expense.money.manager.database.models.CategoryTypesEntity

class CategoryRepository(application: Application) {

    private val db = WealthyDatabase.getDatabase(application)
    private val categoryTypesDao = db.categoryTypesDao()

    fun insertCategoryTypes(categoryTypesEntity: CategoryTypesEntity) {
        categoryTypesDao.insertCategoryTypes(categoryTypesEntity)
    }

    fun loadCategoryTypes(): List<CategoryTypesEntity> {
        return categoryTypesDao.loadCategoryTypes()
    }

    fun loadCategoryTypes(transactionType: String): List<CategoryTypesEntity> {
        return categoryTypesDao.loadCategoryTypes(transactionType)
    }
    fun loadCategoryTypeByID(categoryID: String): CategoryTypesEntity {
        return categoryTypesDao.loadCategoryTypeByID(categoryID = categoryID)
    }

    fun countCategoryTypes(): Int {
        return categoryTypesDao.countCategoryTypes()
    }

    fun updateCategoryTypes(categoryID: String, categoryName: String, categoryDescription: String, transactionType: String, createdAt: String) {
        categoryTypesDao.updateCategoryTypes(
            categoryID = categoryID,
            categoryName = categoryName,
            categoryDescription = categoryDescription,
            transactionType = transactionType,
            createdAt = createdAt
        )
    }

    fun deleteOrArchiveCategoryTypesByID(categoryID: String, categoryStatus: Int) {
        categoryTypesDao.deleteOrArchiveCategoryTypesByID(categoryID = categoryID, categoryStatus = categoryStatus)
    }

    fun deleteCategoryTypes() {
        categoryTypesDao.deleteCategoryTypes()
    }
}
