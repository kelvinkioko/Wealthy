package com.expense.money.manager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.expense.money.manager.database.models.AccountTypesEntity

@Dao
interface AccountTypesDao {
    @Insert
    fun insertAccountTypes(vararg accountTypesEntity: AccountTypesEntity)

    @Query("SELECT * FROM account_types WHERE accountTypeStatus =:accountTypeStatus")
    fun loadAccountTypes(accountTypeStatus: Int): List<AccountTypesEntity>

    @Query("SELECT * FROM account_types WHERE accountTypeID =:accountTypeID")
    fun loadAccountTypeByID(accountTypeID: String): AccountTypesEntity

    @Query("SELECT * FROM account_types WHERE accountTypeName =:accountTypeName")
    fun loadAccountTypeByAccountName(accountTypeName: String): AccountTypesEntity

    @Query("SELECT COUNT(id) FROM account_types")
    fun countAccountTypes(): Int

    @Query("UPDATE account_types SET accountTypeName =:accountTypeName, accountTypeDescription =:accountTypeDescription, createdAt =:createdAt WHERE accountTypeID = :accountTypeID")
    fun updateAccountType(
        accountTypeID: String,
        accountTypeName: String,
        accountTypeDescription: String,
        createdAt: String
    )

    @Query("UPDATE account_types SET accountTypeStatus =:accountTypeStatus WHERE accountTypeID = :accountTypeID")
    fun deleteOrArchiveAccountTypeByID(
        accountTypeID: String,
        accountTypeStatus: Int
    )

    @Query("DELETE FROM account_types")
    fun deleteAccountTypes()
}
