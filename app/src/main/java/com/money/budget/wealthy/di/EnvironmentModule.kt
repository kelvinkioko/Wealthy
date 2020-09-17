package com.money.budget.wealthy.di

import com.money.budget.wealthy.constants.setup.DefaultViewModel
import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.database.repository.CategoryRepository
import com.money.budget.wealthy.database.repository.CurrencyRepository
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.database.repository.TransactionRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val environmentModule = module {
    single { AccountsRepository(get()) }
    single { CategoryRepository(get()) }
    single { CurrencyRepository(get()) }
    single { ExpensesRepository(get()) }
    single { TransactionRepository(get()) }

    viewModel { DefaultViewModel(get()) }
}
