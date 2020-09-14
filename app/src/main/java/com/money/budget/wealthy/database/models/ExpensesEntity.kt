package com.money.budget.wealthy.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "expenses")
data class ExpensesEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "expenseID") var expenseID: String,
    @ColumnInfo(name = "expenseType") var expenseType: String,
    @ColumnInfo(name = "expenseName") var expenseName: String,
    @ColumnInfo(name = "expenseAmount") var expenseAmount: Float,
    @ColumnInfo(name = "expenseAccount") var expenseAccount: String,
    @ColumnInfo(name = "expenseCategory") var expenseCategory: String,
    @ColumnInfo(name = "expenseDescription") var expenseDescription: String,
    @ColumnInfo(name = "expenseImage") var expenseImage: String,
    @ColumnInfo(name = "expenseDate") var expenseDate: String,
    @ColumnInfo(name = "createdAt") var createdAt: String
) : Parcelable
