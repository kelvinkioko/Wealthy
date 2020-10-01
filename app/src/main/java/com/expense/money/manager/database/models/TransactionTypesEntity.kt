package com.expense.money.manager.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "transaction_types")
data class TransactionTypesEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "transactionID") var transactionID: String,
    @ColumnInfo(name = "transactionName") var transactionName: String,
    @ColumnInfo(name = "transactionDescription") var transactionDescription: String,
    @ColumnInfo(name = "createdAt") var createdAt: String
) : Parcelable
