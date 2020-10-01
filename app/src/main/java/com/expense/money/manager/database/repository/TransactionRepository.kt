package com.expense.money.manager.database.repository

import android.app.Application
import com.expense.money.manager.database.WealthyDatabase
import com.expense.money.manager.database.models.TransactionTypesEntity

class TransactionRepository(application: Application) {

    private val db = WealthyDatabase.getDatabase(application)
    private val transactionTypesDao = db.transactionTypesDao()

    fun insertTransactionTypes(transactionTypesEntity: TransactionTypesEntity) {
        transactionTypesDao.insertTransactionTypes(transactionTypesEntity)
    }

    fun loadTransactionTypes(): List<TransactionTypesEntity> {
        return transactionTypesDao.loadTransactionTypes()
    }

    fun countTransactionTypes(): Int {
        return transactionTypesDao.countTransactionTypes()
    }

    fun deleteTransactionTypes() {
        transactionTypesDao.deleteTransactionTypes()
    }
}
