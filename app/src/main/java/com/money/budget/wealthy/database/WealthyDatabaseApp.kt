package com.money.budget.wealthy.database

import android.app.Application
import android.content.Context
import com.money.budget.wealthy.database.dao.AccountTypesDao
import com.money.budget.wealthy.database.dao.AccountsDao
import com.money.budget.wealthy.database.dao.CategoryTypesDao
import com.money.budget.wealthy.database.dao.CurrencyDao
import com.money.budget.wealthy.database.dao.ExpensesDao
import com.money.budget.wealthy.database.dao.TransactionTypesDao

class WealthyDatabaseApp : Application() {

    private lateinit var accountsDao: AccountsDao
    private lateinit var accountTypesDao: AccountTypesDao
    private lateinit var categoryTypesDao: CategoryTypesDao
    private lateinit var currencyDao: CurrencyDao
    private lateinit var expensesDao: ExpensesDao
    private lateinit var transactionTypesDao: TransactionTypesDao

    private lateinit var instance: WealthyDatabaseApp

    override fun onCreate() {
        super.onCreate()
        instance = this
        accountsDao = WealthyDatabase.getDatabase(this).accountsDao()
        accountTypesDao = WealthyDatabase.getDatabase(this).accountTypesDao()
        categoryTypesDao = WealthyDatabase.getDatabase(this).categoryTypesDao()
        currencyDao = WealthyDatabase.getDatabase(this).currencyDao()
        expensesDao = WealthyDatabase.getDatabase(this).expensesDao()
        transactionTypesDao = WealthyDatabase.getDatabase(this).transactionTypesDao()
    }

    @Synchronized
    fun getAccountsDao(context: Context): AccountsDao {
        return WealthyDatabase.getDatabase(context.applicationContext).accountsDao()
    }

    @Synchronized
    fun getAccountTypesDao(context: Context): AccountTypesDao {
        return WealthyDatabase.getDatabase(context.applicationContext).accountTypesDao()
    }

    @Synchronized
    fun getCategoryTypesDao(context: Context): CategoryTypesDao {
        return WealthyDatabase.getDatabase(context.applicationContext).categoryTypesDao()
    }

    @Synchronized
    fun getCurrencyDao(context: Context): CurrencyDao {
        return WealthyDatabase.getDatabase(context.applicationContext).currencyDao()
    }

    @Synchronized
    fun getExpensesDao(context: Context): ExpensesDao {
        return WealthyDatabase.getDatabase(context.applicationContext).expensesDao()
    }

    @Synchronized
    fun getTransactionTypesDao(context: Context): TransactionTypesDao {
        return WealthyDatabase.getDatabase(context.applicationContext).transactionTypesDao()
    }
}
