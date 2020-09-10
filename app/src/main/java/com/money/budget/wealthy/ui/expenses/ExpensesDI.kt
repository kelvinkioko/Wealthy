package com.money.budget.wealthy.ui.expenses

import com.money.budget.wealthy.ui.expenses.manageexpenses.ManageExpenseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val expensesModule = module {
    viewModel { ManageExpenseViewModel(get(), get(), get()) }
}
