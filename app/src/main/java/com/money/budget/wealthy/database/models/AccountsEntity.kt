package com.money.budget.wealthy.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "accounts")
data class AccountsEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "sourceID") var sourceID: String,
    @ColumnInfo(name = "identifier") var identifier: String,
    @ColumnInfo(name = "sourceName") var sourceName: String,
    @ColumnInfo(name = "sourceBalance") var sourceBalance: String,
    @ColumnInfo(name = "sourceNumber") var sourceNumber: String,
    @ColumnInfo(name = "sourceType") var sourceType: String,
    @ColumnInfo(name = "sourceDescription") var sourceDescription: String
) : Parcelable
