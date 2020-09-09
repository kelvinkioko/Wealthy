package com.money.budget.wealthy.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.ui.accounts.account_types.TypeDialogFragment
import com.money.budget.wealthy.ui.accounts.manage_accounts.ManageAccountsFragmentDirections
import com.money.budget.wealthy.util.Event
import com.money.budget.wealthy.util.asEvent

class AccountsViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private lateinit var accountTypesEntity: AccountTypesEntity

    private val _uiState = MutableLiveData<AccountsUIState>()
    val uiState: LiveData<AccountsUIState> = _uiState

    private val _action = MutableLiveData<Event<AccountsActions>>()
    val action: LiveData<Event<AccountsActions>> = _action

    fun loadAccounts() {
        val account = accountsRepository.loadAccounts()
        _uiState.postValue(AccountsUIState.Accounts(account))
    }

    fun loadAccountTypes() {
        val accountTypes = accountsRepository.loadAccountTypes()
        _uiState.postValue(AccountsUIState.AccountTypes(accountTypes))
    }

    fun manageAccounts(accountID: String) {
        _action.postValue(AccountsActions.Navigate(AccountsFragmentDirections.toManageAccountFragment()).asEvent())
    }

    fun addOrEditAccounts(accountID: String) {
        _action.postValue(AccountsActions.Navigate(ManageAccountsFragmentDirections.toAddAccountFragment()).asEvent())
    }

    fun selectAccountType() {
        val bottomSheetFragment = TypeDialogFragment(
            resendAccountTypeCallback = { handleAccountTypeCallBack(it) }
        )
        _action.postValue(AccountsActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    private fun handleAccountTypeCallBack(accountTypesEntity: AccountTypesEntity) {
        this.accountTypesEntity = accountTypesEntity
        _uiState.postValue(AccountsUIState.AccountCallBack(accountTypesEntity))
    }

    fun saveAccounts(account: AccountsEntity) {
        accountsRepository.insertAccounts(accountsEntity = account)
        _uiState.postValue(AccountsUIState.Success)
    }
}

sealed class AccountsActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : AccountsActions()

    data class Navigate(val destination: NavDirections) : AccountsActions()
}

sealed class AccountsUIState {
    object Loading : AccountsUIState()

    object Success : AccountsUIState()

    data class Accounts(val accountEntity: List<AccountsEntity>) : AccountsUIState()

    data class AccountTypes(val accountTypesEntity: List<AccountTypesEntity>) : AccountsUIState()

    data class AccountCallBack(val accountTypesEntity: AccountTypesEntity) : AccountsUIState()

    data class Error(val statusCode: String, val message: String) : AccountsUIState()
}
