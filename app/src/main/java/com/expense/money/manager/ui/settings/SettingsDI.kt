package com.expense.money.manager.ui.settings

import com.expense.money.manager.ui.settings.accounttypes.AccountTypeViewModel
import com.expense.money.manager.ui.settings.currency.CurrencyViewModel
import com.expense.money.manager.ui.settings.expensecategory.ExpenseCategoryViewModel
import com.expense.money.manager.ui.settings.transactiontypes.TransactionTypesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { SettingsViewModel() }
    viewModel { AccountTypeViewModel(get()) }
    viewModel { CurrencyViewModel(get()) }
    viewModel { ExpenseCategoryViewModel(get(), get()) }
    viewModel { TransactionTypesViewModel(get()) }
}
