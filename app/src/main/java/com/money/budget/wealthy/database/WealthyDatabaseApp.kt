package com.money.budget.wealthy.database

import android.app.Application
import android.content.Context
import com.money.budget.wealthy.database.dao.AccountTypesDao
import com.money.budget.wealthy.database.dao.AccountsDao

class WealthyDatabaseApp : Application() {

    private lateinit var accountsDao: AccountsDao
    private lateinit var accountTypesDao: AccountTypesDao

    private lateinit var instance: WealthyDatabaseApp

    override fun onCreate() {
        super.onCreate()
        instance = this
        accountsDao = WealthyDatabase.getDatabase(this).accountsDao()
        accountTypesDao = WealthyDatabase.getDatabase(this).accountTypesDao()
    }

    @Synchronized
    fun getAccountsDao(context: Context): AccountsDao {
        return WealthyDatabase.getDatabase(context.applicationContext).accountsDao()
    }

    @Synchronized
    fun getAccountTypesDao(context: Context): AccountTypesDao {
        return WealthyDatabase.getDatabase(context.applicationContext).accountTypesDao()
    }
}
