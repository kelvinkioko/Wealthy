package com.expense.money.manager.ui.accounts

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountsModule = module {
    viewModel { AccountsViewModel(get()) }
}
