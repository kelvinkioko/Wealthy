package com.money.budget.wealthy.app

import android.app.Application
import com.money.budget.wealthy.BuildConfig
import com.money.budget.wealthy.di.environmentModule
import com.money.budget.wealthy.ui.accounts.accountsModule
import com.money.budget.wealthy.ui.settings.settingsModule
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
                settingsModule
            )
        }
    }
}
