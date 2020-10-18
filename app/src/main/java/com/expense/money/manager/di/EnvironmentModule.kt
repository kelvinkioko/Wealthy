package com.expense.money.manager.di

import com.expense.money.manager.constants.setup.DefaultViewModel
import com.expense.money.manager.database.repository.AccountsRepository
import com.expense.money.manager.database.repository.CategoryRepository
import com.expense.money.manager.database.repository.CurrencyRepository
import com.expense.money.manager.database.repository.ExpensesRepository
import com.expense.money.manager.database.repository.TransactionRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val environmentModule = module {
    single { AccountsRepository(get()) }
    single { CategoryRepository(get()) }
    single { CurrencyRepository(get()) }
    single { ExpensesRepository(get()) }
    single { TransactionRepository(get()) }

    viewModel { DefaultViewModel(get(), get(), get(), get()) }
}
