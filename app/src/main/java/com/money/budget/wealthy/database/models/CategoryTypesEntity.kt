package com.money.budget.wealthy.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "category_types")
data class CategoryTypesEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "categoryID") var categoryID: String,
    @ColumnInfo(name = "categoryName") var categoryName: String,
    @ColumnInfo(name = "categoryDescription") var categoryDescription: String,
    @ColumnInfo(name = "createdAt") var createdAt: String
) : Parcelable
