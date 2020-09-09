package com.money.budget.wealthy.ui.settings

import com.money.budget.wealthy.ui.settings.account_types.AccountTypeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { AccountTypeViewModel(get()) }
}
