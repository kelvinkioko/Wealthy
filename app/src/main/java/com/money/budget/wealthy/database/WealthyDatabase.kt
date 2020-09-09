package com.money.budget.wealthy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.money.budget.wealthy.database.dao.AccountTypesDao
import com.money.budget.wealthy.database.dao.AccountsDao
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.database.models.AccountsEntity

@Database(entities = [AccountsEntity::class, AccountTypesEntity::class], version = 1)
abstract class WealthyDatabase : RoomDatabase() {

    abstract fun accountsDao(): AccountsDao
    abstract fun accountTypesDao(): AccountTypesDao

    companion object {
        @Volatile
        private var INSTANCE: WealthyDatabase? = null

        fun getDatabase(context: Context): WealthyDatabase {
            var tempInstance = INSTANCE
            if (tempInstance == null) {
                tempInstance = Room.databaseBuilder(context.applicationContext,
                    WealthyDatabase::class.java, "wealthy_schema")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return tempInstance
        }
    }
}
