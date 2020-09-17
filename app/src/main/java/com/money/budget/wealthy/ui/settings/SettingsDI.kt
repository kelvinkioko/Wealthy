package com.money.budget.wealthy.ui.settings

import com.money.budget.wealthy.ui.settings.accounttypes.AccountTypeViewModel
import com.money.budget.wealthy.ui.settings.currency.CurrencyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { AccountTypeViewModel(get()) }
    viewModel { CurrencyViewModel(get()) }
}
