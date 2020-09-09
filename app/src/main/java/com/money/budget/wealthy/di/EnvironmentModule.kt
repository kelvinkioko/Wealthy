package com.money.budget.wealthy.di

import com.money.budget.wealthy.database.repository.AccountsRepository
import org.koin.dsl.module

val environmentModule = module {
    single { AccountsRepository(get()) }
}
