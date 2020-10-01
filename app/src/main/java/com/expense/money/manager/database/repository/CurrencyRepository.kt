package com.expense.money.manager.database.repository

import android.app.Application
import com.expense.money.manager.database.WealthyDatabase
import com.expense.money.manager.database.models.CurrencyEntity

class CurrencyRepository(application: Application) {

    private val currencyDao = WealthyDatabase.getDatabase(application).currencyDao()

    fun insertCurrency(currency: CurrencyEntity) {
        currencyDao.insertCurrency(currency)
    }

    fun loadCurrency(): List<CurrencyEntity> {
        return currencyDao.loadCurrency()
    }

    fun countCurrency(): Int {
        return currencyDao.countCurrency()
    }

    fun deleteCurrency() {
        currencyDao.deleteCurrency()
    }
}
