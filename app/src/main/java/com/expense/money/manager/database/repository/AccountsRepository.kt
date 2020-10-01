package com.expense.money.manager.database.repository

import android.app.Application
import com.expense.money.manager.database.WealthyDatabase
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.database.models.AccountsEntity

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

    fun loadAccountByID(sourceID: String): AccountsEntity {
        return accountsDao.loadAccountByID(sourceID)
    }

    fun countAccounts(): Int {
        return accountsDao.countAccounts()
    }

    fun deleteAccountByID(sourceID: String) {
        return accountsDao.deleteAccountByID(sourceID = sourceID)
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
