package com.money.budget.wealthy.di

import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.database.repository.CategoryRepository
import com.money.budget.wealthy.database.repository.TransactionRepository
import org.koin.dsl.module

val environmentModule = module {
    single { AccountsRepository(get()) }
    single { CategoryRepository(get()) }
    single { TransactionRepository(get()) }
}
