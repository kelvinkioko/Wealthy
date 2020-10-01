package com.expense.money.manager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.expense.money.manager.constants.Converters
import com.expense.money.manager.database.dao.AccountTypesDao
import com.expense.money.manager.database.dao.AccountsDao
import com.expense.money.manager.database.dao.CategoryTypesDao
import com.expense.money.manager.database.dao.CurrencyDao
import com.expense.money.manager.database.dao.ExpensesDao
import com.expense.money.manager.database.dao.TransactionTypesDao
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.database.models.AccountsEntity
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.database.models.CurrencyEntity
import com.expense.money.manager.database.models.ExpensesEntity
import com.expense.money.manager.database.models.TransactionTypesEntity

@Database(entities = [AccountsEntity::class, AccountTypesEntity::class, CategoryTypesEntity::class, CurrencyEntity::class, ExpensesEntity::class, TransactionTypesEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WealthyDatabase : RoomDatabase() {

    abstract fun accountsDao(): AccountsDao
    abstract fun accountTypesDao(): AccountTypesDao
    abstract fun categoryTypesDao(): CategoryTypesDao
    abstract fun currencyDao(): CurrencyDao
    abstract fun expensesDao(): ExpensesDao
    abstract fun transactionTypesDao(): TransactionTypesDao

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
