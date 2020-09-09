package com.money.budget.wealthy.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.database.models.AccountTypesEntity
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.ui.accounts.accounttypes.TypeDialogFragment
import com.money.budget.wealthy.ui.accounts.manageaccounts.DeleteAccountDialogFragment
import com.money.budget.wealthy.ui.accounts.manageaccounts.ManageAccountsFragmentDirections
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

    fun loadManageAccounts() {
        val accounts = accountsRepository.loadAccounts()
        val manageAccountsEntity: ArrayList<ManageAccountsEntity> = ArrayList()
        for (account in accounts) {
            manageAccountsEntity.add(
                ManageAccountsEntity(
                    account.sourceID,
                    account.identifier,
                    account.sourceName,
                    account.sourceBalance,
                    account.sourceNumber,
                    account.sourceType,
                    account.sourceDescription,
                    { deleteAccountSheet(account.sourceID) },
                    { addOrEditAccounts(account.sourceID) }
                )
            )
        }
        _uiState.postValue(AccountsUIState.ManageAccounts(manageAccountsEntity))
    }

    fun loadAccountTypes() {
        val accountTypes = accountsRepository.loadAccountTypes()
        _uiState.postValue(AccountsUIState.AccountTypes(accountTypes))
    }

    fun loadAccountByID(accountID: String) {
        val account = accountsRepository.loadAccountByID(accountID)
        _uiState.postValue(AccountsUIState.Account(account))
    }

    private fun deleteAccountSheet(accountID: String) {
        val bottomSheetFragment = DeleteAccountDialogFragment(
            accountID,
            deleteCallBack = { loadManageAccounts() }
        )
        _action.postValue(AccountsActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun deleteAccount(accountID: String) {
        accountsRepository.deleteAccountByID(accountID)
    }

    fun addOrEditAccounts(accountID: String) {
        _action.postValue(AccountsActions.Navigate(ManageAccountsFragmentDirections.toAddAccountFragment(accountID)).asEvent())
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

    data class ManageAccounts(val accountEntity: List<ManageAccountsEntity>) : AccountsUIState()

    data class AccountTypes(val accountTypesEntity: List<AccountTypesEntity>) : AccountsUIState()

    data class AccountCallBack(val accountTypesEntity: AccountTypesEntity) : AccountsUIState()

    data class Account(val accountEntity: AccountsEntity) : AccountsUIState()

    data class Error(val statusCode: String, val message: String) : AccountsUIState()
}

data class ManageAccountsEntity(
    var sourceID: String,
    var identifier: String,
    var sourceName: String,
    var sourceBalance: String,
    var sourceNumber: String,
    var sourceType: String,
    var sourceDescription: String,
    var deleteAccountClick: () -> Unit,
    var editAccountClick: () -> Unit
)
