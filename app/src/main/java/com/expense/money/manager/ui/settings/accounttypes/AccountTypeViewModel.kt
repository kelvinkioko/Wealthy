package com.expense.money.manager.ui.settings.accounttypes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.AccountTypesEntity
import com.expense.money.manager.database.repository.AccountsRepository
import com.expense.money.manager.util.Event
import com.expense.money.manager.util.asEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AccountTypeViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<AccountTypeUIState>()
    val uiState: LiveData<AccountTypeUIState> = _uiState

    private val _action = MutableLiveData<Event<AccountTypeActions>>()
    val action: LiveData<Event<AccountTypeActions>> = _action

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    fun loadAccountTypes() {
        val accountTypes = accountsRepository.loadAccountTypes(accountTypeStatus = StatusEnum.ACTIVE)
        val updatedAccountTypes = accountTypes.map {
            AccountTypesWithActionsEntity(
                id = it.id,
                sourceID = it.accountTypeID,
                accountTypeName = it.accountTypeName,
                accountTypeDescription = it.accountTypeDescription,
                accountTypeStatus = it.accountTypeStatus,
                createdAt = it.createdAt,
                deleteAccountTypeClick = { deleteAccountSheet(it.accountTypeID) },
                editAccountTypeClick = { addOrEditAccountType(it.accountTypeID) })
        }
        _uiState.postValue(AccountTypeUIState.AccountTypes(updatedAccountTypes))
    }

    fun addOrEditAccountType(accountTypeID: String) {
        val bottomSheetFragment = AddAccountTypeDialog(
            accountTypeID = accountTypeID,
            successCallBack = { loadAccountTypes() }
        )
        _action.postValue(AccountTypeActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun loadAccountTypeByID(accountTypeID: String) {
        val accountType = accountsRepository.loadAccountTypeByID(accountTypeID = accountTypeID)
        _uiState.postValue(AccountTypeUIState.AccountTypeDetails(accountType))
    }

    fun saveAccountType(accountType: AccountTypesEntity, update: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                if (update) {
                    accountsRepository.updateAccountType(accountTypeID = accountType.accountTypeID, accountTypeName = accountType.accountTypeName, accountTypeDescription = accountType.accountTypeDescription, createdAt = accountType.createdAt)
                } else {
                    accountsRepository.insertAccountTypes(accountTypesEntity = accountType)
                }
                _uiState.postValue(AccountTypeUIState.Success)
            }
        }
    }

    private fun deleteAccountSheet(accountID: String) {
        val bottomSheetFragment = DeleteAccountTypeDialogFragment(
            accountID,
            deleteCallBack = { loadAccountTypes() }
        )
        _action.postValue(AccountTypeActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun deleteAccount(accountTypeID: String) {
        accountsRepository.deleteOrArchiveAccountTypeByID(accountTypeID = accountTypeID, accountTypeStatus = StatusEnum.DELETED)
    }

    fun archiveAccount(accountTypeID: String) {
        accountsRepository.deleteOrArchiveAccountTypeByID(accountTypeID = accountTypeID, accountTypeStatus = StatusEnum.ARCHIVED)
    }
}

sealed class AccountTypeActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : AccountTypeActions()

    data class Navigate(val destination: NavDirections) : AccountTypeActions()
}

sealed class AccountTypeUIState {
    object Loading : AccountTypeUIState()

    object Success : AccountTypeUIState()

    data class AccountTypes(val accountTypesEntity: List<AccountTypesWithActionsEntity>) : AccountTypeUIState()

    data class AccountTypeDetails(val accountType: AccountTypesEntity) : AccountTypeUIState()

    data class Error(val statusCode: String, val message: String) : AccountTypeUIState()
}

data class AccountTypesWithActionsEntity(
    var id: Int,
    var sourceID: String,
    var accountTypeName: String,
    var accountTypeDescription: String,
    var accountTypeStatus: Int,
    var createdAt: String,
    var deleteAccountTypeClick: () -> Unit,
    var editAccountTypeClick: () -> Unit
)
