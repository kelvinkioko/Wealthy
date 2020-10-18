package com.expense.money.manager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.expense.money.manager.database.models.AccountsEntity

@Dao
interface AccountsDao {
    @Insert
    fun insertAccounts(vararg accountsEntity: AccountsEntity)

    @Query("SELECT * FROM accounts WHERE sourceStatus =:sourceStatus")
    fun loadAccounts(sourceStatus: Int): List<AccountsEntity>

    @Query("SELECT * FROM accounts WHERE sourceID =:sourceID")
    fun loadAccountByID(sourceID: String): AccountsEntity

    @Query("SELECT COUNT(id) FROM accounts")
    fun countAccounts(): Int

    @Query("UPDATE accounts SET identifier =:identifier, sourceName =:sourceName, sourceBalance =:sourceBalance, sourceNumber =:sourceNumber, sourceType =:sourceType, sourceDescription =:sourceDescription, sourceStatus =:sourceStatus, createdAt =:createdAt WHERE sourceID = :sourceID")
    fun updateAccount(
        sourceID: String,
        identifier: String,
        sourceName: String,
        sourceBalance: String,
        sourceNumber: String,
        sourceType: String,
        sourceDescription: String,
        sourceStatus: Int,
        createdAt: String
    )

    @Query("UPDATE accounts SET sourceStatus =:sourceStatus WHERE sourceID = :sourceID")
    fun deleteOrArchiveAccountByID(
        sourceID: String,
        sourceStatus: Int
    )

    @Query("DELETE FROM accounts WHERE sourceID =:sourceID")
    fun deleteAccountByID(sourceID: String)

    @Query("DELETE FROM accounts")
    fun deleteAccounts()
}
