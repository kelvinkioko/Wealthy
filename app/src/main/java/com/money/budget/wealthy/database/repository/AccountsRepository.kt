package com.money.budget.wealthy.database.repository

import android.app.Application
import com.money.budget.wealthy.database.WealthyDatabase
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.database.models.AccountsEntity

class AccountsRepository(application: Application) {

    private val db = WealthyDatabase.getDatabase(application)
    private val accountsDao = db.accountsDao()
    private val accountTypesDao = db.accountTypesDao()

    /**
     * Functions to handle analytics
     */
    fun insertAccounts(accountsEntity: AccountsEntity) {
        accountsDao.insertAccounts(accountsEntity)
    }

    fun loadAccounts(): List<AccountsEntity> {
        return accountsDao.loadAccounts()
    }

    fun countAccounts(): Int {
        return accountsDao.countAccounts()
    }

    fun deleteAccounts() {
        accountsDao.deleteAccounts()
    }

    /**
     *
     */
    fun insertAccountTypes(accountTypesEntity: AccountTypesEntity) {
        accountTypesDao.insertAccountTypes(accountTypesEntity)
    }

    fun loadAccountTypes(): List<AccountTypesEntity> {
        return accountTypesDao.loadAccountTypes()
    }

    fun loadAccountTypeByID(accountID: String): AccountTypesEntity {
        return loadAccountTypeByID(accountID = accountID)
    }

    fun countAccountTypes(): Int {
        return accountTypesDao.countAccountTypes()
    }

    fun deleteAccountTypes() {
        accountTypesDao.deleteAccountTypes()
    }
}
