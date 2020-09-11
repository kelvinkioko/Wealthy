package com.money.budget.wealthy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.money.budget.wealthy.database.models.ExpensesEntity

@Dao
interface ExpensesDao {
    @Insert
    fun insertExpenses(vararg expensesEntity: ExpensesEntity)

    @Query("SELECT * FROM expenses")
    fun loadExpenses(): List<ExpensesEntity>

    @Query("SELECT COUNT(id) FROM expenses")
    fun countExpenses(): Int

    @Query("DELETE FROM expenses")
    fun deleteExpenses()
}
