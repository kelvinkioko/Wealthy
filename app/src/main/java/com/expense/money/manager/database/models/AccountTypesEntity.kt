package com.expense.money.manager.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "account_types")
data class AccountTypesEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "accountID") var sourceID: String,
    @ColumnInfo(name = "accountTypeName") var accountTypeName: String,
    @ColumnInfo(name = "accountDescription") var accountDescription: String,
    @ColumnInfo(name = "createdAt") var createdAt: String
) : Parcelable
