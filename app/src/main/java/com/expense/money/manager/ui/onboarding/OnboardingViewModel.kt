package com.expense.money.manager.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.expense.money.manager.constants.PreferenceHandler
import com.expense.money.manager.database.repository.AccountsRepository
import com.expense.money.manager.util.Event
import com.expense.money.manager.util.asEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OnboardingViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<OnboardingUIState>()
    val uiState: LiveData<OnboardingUIState> = _uiState

    private val _action = MutableLiveData<Event<OnboardingActions>>()
    val action: LiveData<Event<OnboardingActions>> = _action

    fun resetDataBasedOnPrefs() {
        _uiState.postValue(OnboardingUIState.Loading)
    }

    fun currencySetup() {
        _action.postValue(OnboardingActions.Navigate(OnboardingSetupFragmentDirections.toSettingsCurrencyFragment()).asEvent())
    }

    fun accountTypesSetup() {
        _action.postValue(OnboardingActions.Navigate(OnboardingSetupFragmentDirections.toSettingsAccountTypesFragment()).asEvent())
    }

    fun accountsSetup() {
        _action.postValue(OnboardingActions.Navigate(OnboardingSetupFragmentDirections.toManageAccountFragment()).asEvent())
    }

    fun expenseTypesSetup() {
        _action.postValue(OnboardingActions.Navigate(OnboardingSetupFragmentDirections.toExpenseCategoryFragment()).asEvent())
    }

    fun validateAccountTypesSetup(preferenceHandler: PreferenceHandler): Boolean {
        return (preferenceHandler.getAccountTypesSetup()!! && (accountsRepository.countAccountTypes() > 0))
    }

    fun validateAccountSetup(preferenceHandler: PreferenceHandler): Boolean {
        return (preferenceHandler.getAccountSetup()!! && (accountsRepository.countAccounts() > 0))
    }
}

sealed class OnboardingActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : OnboardingActions()

    data class Navigate(val destination: NavDirections) : OnboardingActions()
}

sealed class OnboardingUIState {
    object Loading : OnboardingUIState()
}
