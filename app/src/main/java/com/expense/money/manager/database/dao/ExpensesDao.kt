package com.expense.money.manager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.expense.money.manager.database.models.ExpensesEntity
import com.expense.money.manager.ui.statistics.StatisticsExpenseItem
import java.util.Date

@Dao
interface ExpensesDao {
    @Insert
    fun insertExpenses(vararg expensesEntity: ExpensesEntity)

    @Query("SELECT * FROM expenses WHERE expenseDate LIKE :monthYearVal ORDER BY expenseDate ASC")
    fun loadExpenses(monthYearVal: String): List<ExpensesEntity>

    @Query("SELECT * FROM expenses WHERE expenseDate BETWEEN :startDate AND :endDate ORDER BY expenseDate ASC")
    fun loadExpenses(startDate: Date, endDate: Date): List<ExpensesEntity>

    @Query("SELECT expenseType, sum(expenseAmount) as expenseAmount, expenseCategory FROM expenses where expenseDate BETWEEN :startDate AND :endDate AND expenseType LIKE :expenseType GROUP BY expenseCategory ORDER BY expenseDate ASC")
    fun loadExpensesByDateAndExpense(startDate: Date, endDate: Date, expenseType: String): List<StatisticsExpenseItem>

    @Query("SELECT SUM(expenseAmount) as Total FROM expenses where expenseDate =:expenseDate AND expenseType LIKE '%Expense%'")
    fun loadExpensesByDate(expenseDate: Date): Float

    @Query("SELECT sum(expenseAmount) FROM expenses where expenseDate BETWEEN :startDate AND :endDate AND expenseType LIKE '%Expense%' ORDER BY expenseDate ASC")
    fun loadAnnualExpensesByDate(startDate: Date, endDate: Date): Float

    @Query("SELECT sum(expenseAmount) FROM expenses where expenseDate =:expenseDate AND expenseType LIKE '%Income%'")
    fun loadIncomeByDate(expenseDate: Date): Float

    @Query("SELECT sum(expenseAmount) FROM expenses where expenseDate BETWEEN :startDate AND :endDate AND expenseType LIKE '%Income%' ORDER BY expenseDate ASC")
    fun loadAnnualIncomeByDate(startDate: Date, endDate: Date): Float

    @Query("SELECT COUNT(id) FROM expenses")
    fun countExpenses(): Int

    @Query("SELECT COUNT(id) FROM expenses where expenseDate BETWEEN :startDate AND :endDate ORDER BY expenseDate ASC")
    fun loadExpensesByRange(startDate: Date, endDate: Date): Int

    @Query("DELETE FROM expenses")
    fun deleteExpenses()
}
