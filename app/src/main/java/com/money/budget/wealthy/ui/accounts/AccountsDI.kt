package com.money.budget.wealthy.ui.accounts

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountsModule = module {
    viewModel { AccountsViewModel(get()) }
}
