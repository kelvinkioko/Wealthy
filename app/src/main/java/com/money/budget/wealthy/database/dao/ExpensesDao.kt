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

    @Query("SELECT SUM(expenseAmount) as Total FROM expenses where expenseDate =:expenseDate AND expenseType LIKE '%Expense%'")
    fun loadExpensesByDate(expenseDate: String): Float

    @Query("SELECT sum(expenseAmount) FROM expenses where expenseDate =:expenseDate AND expenseType LIKE '%Income%'")
    fun loadIncomeByDate(expenseDate: String): Float

    @Query("SELECT COUNT(id) FROM expenses")
    fun countExpenses(): Int

    @Query("DELETE FROM expenses")
    fun deleteExpenses()
}
