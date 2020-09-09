package com.money.budget.wealthy.ui.settings.accounttypes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.util.Event
import com.money.budget.wealthy.util.asEvent

class AccountTypeViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<AccountTypeUIState>()
    val uiState: LiveData<AccountTypeUIState> = _uiState

    private val _action = MutableLiveData<Event<AccountTypeActions>>()
    val action: LiveData<Event<AccountTypeActions>> = _action

    fun loadAccountTypes() {
        val accountTypes = accountsRepository.loadAccountTypes()
        _uiState.postValue(AccountTypeUIState.AccountTypes(accountTypes))
    }

    fun addOrEditAccountType(accountID: String) {
        val bottomSheetFragment = AddAccountTypeDialog(accountID)
        _action.postValue(AccountTypeActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun saveAccountType(accountType: AccountTypesEntity) {
        accountsRepository.insertAccountTypes(accountTypesEntity = accountType)
        _uiState.postValue(AccountTypeUIState.Success)
    }
}

sealed class AccountTypeActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : AccountTypeActions()

    data class Navigate(val destination: NavDirections) : AccountTypeActions()
}

sealed class AccountTypeUIState {
    object Loading : AccountTypeUIState()

    object Success : AccountTypeUIState()

    data class AccountTypes(val accountTypesEntity: List<AccountTypesEntity>) : AccountTypeUIState()

    data class Error(val statusCode: String, val message: String) : AccountTypeUIState()
}
