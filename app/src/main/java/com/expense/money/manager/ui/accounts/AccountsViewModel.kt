package com.expense.money.manager.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.database.models.AccountsEntity
import com.expense.money.manager.database.repository.AccountsRepository
import com.expense.money.manager.ui.accounts.accounttypes.TypeDialogFragment
import com.expense.money.manager.ui.accounts.manageaccounts.DeleteAccountDialogFragment
import com.expense.money.manager.ui.accounts.manageaccounts.ManageAccountsFragmentDirections
import com.expense.money.manager.ui.settings.accounttypes.AddAccountTypeDialog
import com.expense.money.manager.util.Event
import com.expense.money.manager.util.asEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AccountsViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private lateinit var accountTypesEntity: AccountTypesEntity

    private val _uiState = MutableLiveData<AccountsUIState>()
    val uiState: LiveData<AccountsUIState> = _uiState

    private val _action = MutableLiveData<Event<AccountsActions>>()
    val action: LiveData<Event<AccountsActions>> = _action

    fun loadAccounts() {
        val accounts = accountsRepository.loadAccounts()

        val sectionedAccountDetailsItem = mutableListOf<SectionedAccountDetailsItem>()
        var previousSource = ""
        accounts.forEach {
            val currentSource = it.sourceType
            if (previousSource.isNullOrEmpty() || previousSource != currentSource) {
                previousSource = currentSource
                sectionedAccountDetailsItem.add(SectionedAccountDetailsItem.AccountsHeader(currentSource))
            }
            sectionedAccountDetailsItem.add(
                SectionedAccountDetailsItem.AccountsEntity(
                    sourceID = it.sourceID,
                    identifier = it.identifier,
                    sourceName = it.sourceName,
                    sourceBalance = it.sourceBalance,
                    sourceNumber = it.sourceNumber,
                    sourceType = it.sourceType,
                    sourceDescription = it.sourceDescription
                ))
        }
        _uiState.postValue(AccountsUIState.Accounts(sectionedAccountDetailsItem))
    }

    fun loadManageAccounts() {
        val accounts = accountsRepository.loadAccounts()

        val sectionedAccountItem = mutableListOf<SectionedAccountItem>()
        var previousSource = ""
        accounts.forEach {
            val currentSource = it.sourceType
            if (previousSource.isNullOrEmpty() || previousSource != currentSource) {
                previousSource = currentSource
                sectionedAccountItem.add(SectionedAccountItem.ManageAccountsHeader(currentSource))
            }
            sectionedAccountItem.add(
                SectionedAccountItem.ManageAccountsEntity(
                    sourceID = it.sourceID,
                    identifier = it.identifier,
                    sourceName = it.sourceName,
                    sourceBalance = it.sourceBalance,
                    sourceNumber = it.sourceNumber,
                    sourceType = it.sourceType,
                    sourceDescription = it.sourceDescription,
                    deleteAccountClick = { deleteAccountSheet(it.sourceID) },
                    editAccountClick = { addOrEditAccounts(it.sourceID) }
                ))
        }
        _uiState.postValue(AccountsUIState.ManageAccounts(sectionedAccountItem))
    }

    fun onManageClicked() {
        val directions = if (accountsRepository.countAccounts() > 0) {
            AccountsFragmentDirections.toManageAccountFragment()
        } else {
            AccountsFragmentDirections.toAddAccountFragment(accountID = "")
        }
        _action.postValue(AccountsActions.Navigate(directions).asEvent())
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
            resendAccountTypeCallback = { handleAccountTypeCallBack(it) },
            addAccountCallback = { addOrEditAccountType("") }
        )
        _action.postValue(AccountsActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    private fun addOrEditAccountType(accountTypeID: String) {
        val bottomSheetFragment = AddAccountTypeDialog(
            accountTypeID = accountTypeID,
            successCallBack = { loadAccountTypes() }
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

    data class Accounts(val accountEntity: List<SectionedAccountDetailsItem>) : AccountsUIState()

    data class ManageAccounts(val accountEntity: List<SectionedAccountItem>) : AccountsUIState()

    object AccountTypeAdded : AccountsUIState()

    data class AccountTypes(val accountTypesEntity: List<AccountTypesEntity>) : AccountsUIState()

    data class AccountCallBack(val accountTypesEntity: AccountTypesEntity) : AccountsUIState()

    data class Account(val accountEntity: AccountsEntity) : AccountsUIState()

    data class Error(val statusCode: String, val message: String) : AccountsUIState()
}

sealed class SectionedAccountItem {
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
    ) : SectionedAccountItem()

    data class ManageAccountsHeader(val title: String) : SectionedAccountItem()
}

sealed class SectionedAccountDetailsItem {
    data class AccountsEntity(
        var sourceID: String,
        var identifier: String,
        var sourceName: String,
        var sourceBalance: String,
        var sourceNumber: String,
        var sourceType: String,
        var sourceDescription: String
    ) : SectionedAccountDetailsItem()

    data class AccountsHeader(val title: String) : SectionedAccountDetailsItem()
}
