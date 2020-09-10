package com.money.budget.wealthy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.money.budget.wealthy.database.models.CategoryTypesEntity

@Dao
interface CategoryTypesDao {
    @Insert
    fun insertCategoryTypes(vararg categoryTypesEntity: CategoryTypesEntity)

    @Query("SELECT * FROM category_types")
    fun loadCategoryTypes(): List<CategoryTypesEntity>

    @Query("SELECT COUNT(id) FROM category_types")
    fun countCategoryTypes(): Int

    @Query("DELETE FROM category_types")
    fun deleteCategoryTypes()
}
