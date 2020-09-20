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

    fun loadExpensesByDate(expenseDate: Date): Float {
        return expensesDao.loadExpensesByDate(expenseDate)
    }

    fun loadIncomeByDate(expenseDate: Date): Float {
        return expensesDao.loadIncomeByDate(expenseDate)
    }

    fun countExpenses(): Int {
        return expensesDao.countExpenses()
    }

    fun deleteExpenses() {
        expensesDao.deleteExpenses()
    }
}
