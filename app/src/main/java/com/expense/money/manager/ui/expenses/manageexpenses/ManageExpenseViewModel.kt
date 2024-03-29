package com.expense.money.manager.ui.expenses.manageexpenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.constants.StatusEnum
import com.expense.money.manager.database.models.AccountsEntity
import com.expense.money.manager.database.models.CategoryTypesEntity
import com.expense.money.manager.database.models.ExpensesEntity
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.database.repository.AccountsRepository
import com.expense.money.manager.database.repository.CategoryRepository
import com.expense.money.manager.database.repository.ExpensesRepository
import com.expense.money.manager.database.repository.TransactionRepository
import com.expense.money.manager.ui.expenses.manageexpenses.choose.ChooseAccountDialogFragment
import com.expense.money.manager.ui.expenses.manageexpenses.choose.ChooseCategoryDialogFragment
import com.expense.money.manager.util.Event
import com.expense.money.manager.util.asEvent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ManageExpenseViewModel(
    private val accountsRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
    private val expensesRepository: ExpensesRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<ManageExpenseUIState>()
    val uiState: LiveData<ManageExpenseUIState> = _uiState

    private val _action = MutableLiveData<Event<ManageExpenseActions>>()
    val action: LiveData<Event<ManageExpenseActions>> = _action

    private lateinit var transactionTypesEntity: TransactionTypesEntity
    private lateinit var categoryTypesEntity: CategoryTypesEntity
    private lateinit var accountsEntity: AccountsEntity

    fun loadTransactionTypes() {
        val transactionTypes = transactionRepository.loadTransactionTypes()
        _uiState.postValue(ManageExpenseUIState.TransactionTypes(transactionTypes))
    }

    fun setTransactionTypes(transactionTypesEntity: TransactionTypesEntity) {
        this.transactionTypesEntity = transactionTypesEntity
    }

    fun chooseCategory() {
        val bottomSheetFragment = ChooseCategoryDialogFragment(
            loadCategories(),
            resendCategoryCallback = {
                this.categoryTypesEntity = it
                _uiState.postValue(ManageExpenseUIState.CategoryTypes(it))
            }
        )
        _action.postValue(ManageExpenseActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    private fun loadCategories(): List<CategoryTypesEntity> {
        return if (this::transactionTypesEntity.isInitialized) {
            categoryRepository.loadCategoryTypes("${transactionTypesEntity.transactionName}#${transactionTypesEntity.transactionID}")
        } else {
            categoryRepository.loadCategoryTypes()
        }
    }

    fun chooseAccount() {
        val bottomSheetFragment = ChooseAccountDialogFragment(
            accountsRepository.loadAccounts(sourceStatus = StatusEnum.ACTIVE),
            resendAccountCallback = {
                this.accountsEntity = it
                _uiState.postValue(ManageExpenseUIState.Accounts(it))
            },
            createAccountCallback = {
                _action.postValue(ManageExpenseActions.Navigate(AddExpenseFragmentDirections.toAddAccountFragment(accountID = "")).asEvent())
            }
        )
        _action.postValue(ManageExpenseActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun saveExpense(name: String, amount: String, description: String, date: String) {
        expensesRepository.insertExpenses(
            ExpensesEntity(
                id = 0,
                expenseID = "EXP-${Hive().getTimestamp()}",
                expenseType = "${transactionTypesEntity.transactionName}#${transactionTypesEntity.transactionID}",
                expenseName = name,
                expenseAmount = amount.toFloat(),
                expenseAccount = "${accountsEntity.sourceName}#${accountsEntity.sourceID}",
                expenseCategory = "${categoryTypesEntity.categoryName}#${categoryTypesEntity.categoryID}",
                expenseDescription = description,
                expenseImage = "",
                expenseDate = Hive().getDateFromString(date),
                createdAt = Hive().getCurrentDateTime()
            )
        )
        _uiState.postValue(ManageExpenseUIState.Success)
    }
}

sealed class ManageExpenseActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : ManageExpenseActions()

    data class Navigate(val destination: NavDirections) : ManageExpenseActions()
}

sealed class ManageExpenseUIState {
    object Loading : ManageExpenseUIState()

    object Success : ManageExpenseUIState()

    data class TransactionTypes(val transactionTypesEntity: List<TransactionTypesEntity>) : ManageExpenseUIState()

    data class CategoryTypes(val categoryTypesEntity: CategoryTypesEntity) : ManageExpenseUIState()

    data class Accounts(val accountsEntity: AccountsEntity) : ManageExpenseUIState()

    data class Error(val statusCode: String, val message: String) : ManageExpenseUIState()
}
