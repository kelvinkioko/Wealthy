package com.money.budget.wealthy.ui.expenses.manageexpenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.database.models.CategoryTypesEntity
import com.money.budget.wealthy.database.models.ExpensesEntity
import com.money.budget.wealthy.database.models.TransactionTypesEntity
import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.database.repository.CategoryRepository
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.database.repository.TransactionRepository
import com.money.budget.wealthy.ui.expenses.manageexpenses.choose.ChooseAccountDialogFragment
import com.money.budget.wealthy.ui.expenses.manageexpenses.choose.ChooseCategoryDialogFragment
import com.money.budget.wealthy.util.Event
import com.money.budget.wealthy.util.asEvent

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

    init {
        populateDummyContent()
    }

    private fun populateDummyContent() {
        if (transactionRepository.countTransactionTypes() == 0) {
            val income = TransactionTypesEntity(
                id = 0,
                transactionID = "TTE-${Hive().getTimestamp()}",
                transactionName = "Income",
                transactionDescription = "Money coming into you account, wallet or other personal sources",
                createdAt = Hive().getCurrentDateTime()
            )

            transactionRepository.insertTransactionTypes(income)

            val expense = TransactionTypesEntity(
                id = 0,
                transactionID = "TTE-${Hive().getTimestamp()}",
                transactionName = "Expense",
                transactionDescription = "Money spent from your account",
                createdAt = Hive().getCurrentDateTime()
            )

            transactionRepository.insertTransactionTypes(expense)

            val transfer = TransactionTypesEntity(
                id = 0,
                transactionID = "TTE-${Hive().getTimestamp()}",
                transactionName = "Transfer",
                transactionDescription = "Money moved from one account to another",
                createdAt = Hive().getCurrentDateTime()
            )

            transactionRepository.insertTransactionTypes(transfer)
        }

        if (categoryRepository.countCategoryTypes() == 0) {
            val categoryOne = CategoryTypesEntity(
                id = 0,
                categoryID = "CAT-${Hive().getTimestamp()}",
                categoryName = "Food",
                categoryDescription = "Food",
                createdAt = Hive().getCurrentDateTime()
            )

            categoryRepository.insertCategoryTypes(categoryOne)

            val categoryTwo = CategoryTypesEntity(
                id = 0,
                categoryID = "CAT-${Hive().getTimestamp()}",
                categoryName = "Social life",
                categoryDescription = "Social",
                createdAt = Hive().getCurrentDateTime()
            )

            categoryRepository.insertCategoryTypes(categoryTwo)

            val categoryThree = CategoryTypesEntity(
                id = 0,
                categoryID = "CAT-${Hive().getTimestamp()}",
                categoryName = "Transportation",
                categoryDescription = "Movement from one destination to another",
                createdAt = Hive().getCurrentDateTime()
            )

            categoryRepository.insertCategoryTypes(categoryThree)

            val categoryFour = CategoryTypesEntity(
                id = 0,
                categoryID = "CAT-${Hive().getTimestamp()}",
                categoryName = "Education",
                categoryDescription = "education",
                createdAt = Hive().getCurrentDateTime()
            )

            categoryRepository.insertCategoryTypes(categoryFour)
        }
    }

    fun loadTransactionTypes() {
        val transactionTypes = transactionRepository.loadTransactionTypes()
        _uiState.postValue(ManageExpenseUIState.TransactionTypes(transactionTypes))
    }

    fun setTransactionTypes(transactionTypesEntity: TransactionTypesEntity) {
        this.transactionTypesEntity = transactionTypesEntity
    }

    fun chooseCategory() {
        val bottomSheetFragment = ChooseCategoryDialogFragment(
            categoryRepository.loadCategoryTypes(),
            resendCategoryCallback = {
                this.categoryTypesEntity = it
                _uiState.postValue(ManageExpenseUIState.CategoryTypes(it))
            }
        )
        _action.postValue(ManageExpenseActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun chooseAccount() {
        val bottomSheetFragment = ChooseAccountDialogFragment(
            accountsRepository.loadAccounts(),
            resendAccountCallback = {
                this.accountsEntity = it
                _uiState.postValue(ManageExpenseUIState.Accounts(it))
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
                expenseAmount = amount,
                expenseAccount = "${accountsEntity.sourceName}#${accountsEntity.sourceID}",
                expenseCategory = "${categoryTypesEntity.categoryName}#${categoryTypesEntity.categoryID}",
                expenseDescription = description,
                expenseImage = "",
                expenseDate = date,
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
