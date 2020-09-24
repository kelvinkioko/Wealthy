package com.money.budget.wealthy.ui.statistics

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val statisticsModule = module {
    viewModel { StatisticsViewModel(get(), get(), get(), get()) }
}
