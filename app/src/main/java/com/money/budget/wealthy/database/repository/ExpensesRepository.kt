package com.money.budget.wealthy.database.repository

import android.app.Application
import com.money.budget.wealthy.database.WealthyDatabase
import com.money.budget.wealthy.database.models.ExpensesEntity

class ExpensesRepository(application: Application) {

    private val expensesDao = WealthyDatabase.getDatabase(application).expensesDao()

    fun insertExpenses(expensesEntity: ExpensesEntity) {
        expensesDao.insertExpenses(expensesEntity)
    }

    fun loadExpenses(): List<ExpensesEntity> {
        return expensesDao.loadExpenses()
    }

    fun loadExpensesByDate(expenseDate: String): Float {
        return expensesDao.loadExpensesByDate(expenseDate)
    }

    fun loadIncomeByDate(expenseDate: String): Float {
        return expensesDao.loadIncomeByDate(expenseDate)
    }

    fun countExpenses(): Int {
        return expensesDao.countExpenses()
    }

    fun deleteExpenses() {
        expensesDao.deleteExpenses()
    }
}
