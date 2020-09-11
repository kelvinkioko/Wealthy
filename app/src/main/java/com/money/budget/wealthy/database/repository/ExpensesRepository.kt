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

    fun countExpenses(): Int {
        return expensesDao.countExpenses()
    }

    fun deleteExpenses() {
        expensesDao.deleteExpenses()
    }
}
