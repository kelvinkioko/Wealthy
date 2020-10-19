package com.expense.money.manager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.expense.money.manager.database.models.TransactionTypesEntity

@Dao
interface TransactionTypesDao {
    @Insert
    fun insertTransactionTypes(vararg transactionTypesEntity: TransactionTypesEntity)

    @Query("SELECT * FROM transaction_types")
    fun loadTransactionTypes(): List<TransactionTypesEntity>

    @Query("SELECT * FROM transaction_types WHERE transactionID =:transactionID")
    fun loadTransactionType(transactionID: String): TransactionTypesEntity

    @Query("SELECT COUNT(id) FROM transaction_types")
    fun countTransactionTypes(): Int

    @Query("DELETE FROM transaction_types")
    fun deleteTransactionTypes()
}
