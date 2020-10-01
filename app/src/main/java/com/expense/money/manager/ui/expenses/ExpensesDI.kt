package com.expense.money.manager.ui.expenses

import com.expense.money.manager.ui.expenses.daily.DailyExpensesViewModel
import com.expense.money.manager.ui.expenses.manageexpenses.ManageExpenseViewModel
import com.expense.money.manager.ui.expenses.monthly.MonthlyExpensesViewModel
import com.expense.money.manager.ui.expenses.weekly.WeeklyExpensesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val expensesModule = module {
    viewModel { ManageExpenseViewModel(get(), get(), get(), get()) }
    viewModel { DailyExpensesViewModel(get()) }
    viewModel { WeeklyExpensesViewModel(get()) }
    viewModel { MonthlyExpensesViewModel(get()) }
}
