package com.money.budget.wealthy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.money.budget.wealthy.database.models.AccountsEntity

@Dao
interface AccountsDao {
    @Insert
    fun insertAccounts(vararg accountsEntity: AccountsEntity)

    @Query("SELECT * FROM accounts")
    fun loadAccounts(): List<AccountsEntity>

    @Query("SELECT COUNT(id) FROM accounts")
    fun countAccounts(): Int

    @Query("DELETE FROM accounts")
    fun deleteAccounts()
}
