package com.expense.money.manager.app

import android.app.Application
import com.expense.money.manager.BuildConfig
import com.expense.money.manager.di.environmentModule
import com.expense.money.manager.ui.accounts.accountsModule
import com.expense.money.manager.ui.expenses.expensesModule
import com.expense.money.manager.ui.onboarding.onBoardingModule
import com.expense.money.manager.ui.settings.settingsModule
import com.expense.money.manager.ui.statistics.statisticsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class WealthyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@WealthyApp)
            modules(
                environmentModule,
                accountsModule,
                expensesModule,
                statisticsModule,
                settingsModule,
                onBoardingModule
            )
        }
    }
}
