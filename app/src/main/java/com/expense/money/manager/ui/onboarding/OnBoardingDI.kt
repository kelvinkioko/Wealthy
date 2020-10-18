package com.expense.money.manager.ui.onboarding

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {
    viewModel { OnboardingViewModel(get()) }
}
