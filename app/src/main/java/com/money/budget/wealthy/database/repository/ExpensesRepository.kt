package com.money.budget.wealthy.database.repository

import android.app.Application
import com.money.budget.wealthy.database.WealthyDatabase
import com.money.budget.wealthy.database.models.ExpensesEntity
import java.util.Date

class ExpensesRepository(application: Application) {

    private val expensesDao = WealthyDatabase.getDatabase(application).expensesDao()

    fun insertExpenses(expensesEntity: ExpensesEntity) {
        expensesDao.insertExpenses(expensesEntity)
    }

    fun loadExpenses(monthYearVal: String): List<ExpensesEntity> {
        return expensesDao.loadExpenses("%$monthYearVal%")
    }

    fun loadExpenses(startDate: Date, endDate: Date): List<ExpensesEntity> {
        return expensesDao.loadExpenses(startDate, endDate)
    }

    fun loadExpensesByDateAndExpense(startDate: Date, endDate: Date, expenseType: String): List<ExpensesEntity> {
        return expensesDao.loadExpensesByDateAndExpense(startDate, endDate, "%$expenseType")
    }

    fun loadExpensesByDate(expenseDate: Date): Float {
        return expensesDao.loadExpensesByDate(expenseDate)
    }

    fun loadAnnualExpensesByDate(startDate: Date, endDate: Date): Float {
        return expensesDao.loadAnnualExpensesByDate(startDate, endDate)
    }

    fun loadIncomeByDate(expenseDate: Date): Float {
        return expensesDao.loadIncomeByDate(expenseDate)
    }

    fun loadAnnualIncomeByDate(startDate: Date, endDate: Date): Float {
        return expensesDao.loadAnnualIncomeByDate(startDate, endDate)
    }

    fun countExpenses(): Int {
        return expensesDao.countExpenses()
    }

    fun countExpensesByRange(startDate: Date, endDate: Date): Int {
        return expensesDao.loadExpensesByRange(startDate, endDate)
    }

    fun deleteExpenses() {
        expensesDao.deleteExpenses()
    }
}
