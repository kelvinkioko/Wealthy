package com.expense.money.manager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.expense.money.manager.database.models.AccountTypesEntity

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

    @Query("UPDATE account_types SET accountTypeName =:accountTypeName, accountDescription =:accountDescription, createdAt =:createdAt WHERE accountID = :accountID")
    fun updateAccountType(
        accountID: String,
        accountTypeName: String,
        accountDescription: String,
        createdAt: String
    )

    @Query("DELETE FROM account_types")
    fun deleteAccountTypes()
}
