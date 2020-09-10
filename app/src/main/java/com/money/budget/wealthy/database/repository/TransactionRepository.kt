package com.money.budget.wealthy.database.repository

import android.app.Application
import com.money.budget.wealthy.database.WealthyDatabase
import com.money.budget.wealthy.database.models.TransactionTypesEntity

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
