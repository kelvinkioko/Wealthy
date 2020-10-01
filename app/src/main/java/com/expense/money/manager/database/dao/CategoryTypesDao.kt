package com.expense.money.manager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.expense.money.manager.database.models.CategoryTypesEntity

@Dao
interface CategoryTypesDao {
    @Insert
    fun insertCategoryTypes(vararg categoryTypesEntity: CategoryTypesEntity)

    @Query("SELECT * FROM category_types")
    fun loadCategoryTypes(): List<CategoryTypesEntity>

    @Query("SELECT * FROM category_types WHERE transactionType =:transactionType")
    fun loadCategoryTypes(transactionType: String): List<CategoryTypesEntity>

    @Query("SELECT COUNT(id) FROM category_types")
    fun countCategoryTypes(): Int

    @Query("DELETE FROM category_types")
    fun deleteCategoryTypes()
}
