package com.money.budget.wealthy.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.money.budget.wealthy.database.models.AccountTypesEntity

@Dao
interface AccountTypesDao {
    @Insert
    fun insertAccountTypes(vararg accountTypesEntity: AccountTypesEntity)

    @Query("SELECT * FROM account_types")
    fun loadAccountTypes(): List<AccountTypesEntity>

    @Query("SELECT * FROM account_types WHERE accountID =:accountID")
    fun loadAccountTypeByID(accountID: String): AccountTypesEntity

    @Query("SELECT COUNT(id) FROM account_types")
    fun countAccountTypes(): Int

    @Query("DELETE FROM account_types")
    fun deleteAccountTypes()
}
