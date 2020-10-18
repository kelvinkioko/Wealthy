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

    fun loadAccounts(sourceStatus: Int): List<AccountsEntity> {
        return accountsDao.loadAccounts(sourceStatus = sourceStatus)
    }

    fun loadAccountByID(sourceID: String): AccountsEntity {
        return accountsDao.loadAccountByID(sourceID)
    }

    fun countAccounts(): Int {
        return accountsDao.countAccounts()
    }

    fun updateAccount(sourceID: String, identifier: String, sourceName: String, sourceBalance: String, sourceNumber: String, sourceType: String, sourceDescription: String, sourceStatus: Int, createdAt: String) {
        accountsDao.updateAccount(
            sourceID = sourceID,
            identifier = identifier,
            sourceName = sourceName,
            sourceBalance = sourceBalance,
            sourceNumber = sourceNumber,
            sourceType = sourceType,
            sourceDescription = sourceDescription,
            sourceStatus = sourceStatus,
            createdAt = createdAt
        )
    }

    fun deleteOrArchiveAccountByID(sourceID: String, sourceStatus: Int) {
        accountsDao.deleteOrArchiveAccountByID(sourceID = sourceID, sourceStatus = sourceStatus)
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

    fun loadAccountTypes(accountTypeStatus: Int): List<AccountTypesEntity> {
        return accountTypesDao.loadAccountTypes(accountTypeStatus = accountTypeStatus)
    }

    fun loadAccountTypeByID(accountTypeID: String): AccountTypesEntity {
        return accountTypesDao.loadAccountTypeByID(accountTypeID = accountTypeID)
    }

    fun loadAccountTypeByAccountName(accountTypeName: String): AccountTypesEntity {
        return accountTypesDao.loadAccountTypeByAccountName(accountTypeName = accountTypeName)
    }

    fun countAccountTypes(): Int {
        return accountTypesDao.countAccountTypes()
    }

    fun updateAccountType(accountTypeID: String, accountTypeName: String, accountTypeDescription: String, createdAt: String) {
        accountTypesDao.updateAccountType(accountTypeID = accountTypeID, accountTypeName = accountTypeName, accountTypeDescription = accountTypeDescription, createdAt = createdAt)
    }

    fun deleteOrArchiveAccountTypeByID(accountTypeID: String, accountTypeStatus: Int) {
        accountTypesDao.deleteOrArchiveAccountTypeByID(
            accountTypeID = accountTypeID,
            accountTypeStatus = accountTypeStatus
        )
    }

    fun deleteAccountTypes() {
        accountTypesDao.deleteAccountTypes()
    }
}
