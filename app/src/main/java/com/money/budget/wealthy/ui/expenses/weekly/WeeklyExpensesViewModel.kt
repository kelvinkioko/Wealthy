package com.money.budget.wealthy.ui.expenses.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.util.Event

class WeeklyExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<WeeklyExpensesUIState>()
    val uiState: LiveData<WeeklyExpensesUIState> = _uiState

    private val _action = MutableLiveData<Event<WeeklyExpensesActions>>()
    val action: LiveData<Event<WeeklyExpensesActions>> = _action

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        val transactions = expensesRepository.loadExpenses()
        val displayTransactionsEntity: ArrayList<WeeklyTransactionsEntity> = ArrayList()
        for (transaction in transactions) {
            displayTransactionsEntity.add(
                WeeklyTransactionsEntity(
                    expenseID = transaction.expenseID,
                    expenseType = transaction.expenseType,
                    expenseName = transaction.expenseName,
                    expenseAmount = transaction.expenseAmount,
                    expenseAccount = transaction.expenseAccount,
                    expenseCategory = transaction.expenseCategory,
                    expenseDescription = transaction.expenseDescription,
                    expenseImage = transaction.expenseImage,
                    expenseDate = transaction.expenseDate,
                    createdAt = transaction.createdAt,
                    TotalExpense = expensesRepository.loadExpensesByDate(transaction.expenseDate).toString(),
                    TotalIncome = expensesRepository.loadIncomeByDate(transaction.expenseDate).toString()
                )
            )

            println("Total ${expensesRepository.loadExpensesByDate(transaction.expenseDate)}")
            println("Total ${expensesRepository.loadIncomeByDate(transaction.expenseDate)}")
        }
        _uiState.postValue(WeeklyExpensesUIState.WeeklyExpenses(displayTransactionsEntity))
    }
}

sealed class WeeklyExpensesActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : WeeklyExpensesActions()

    data class Navigate(val destination: NavDirections) : WeeklyExpensesActions()
}

sealed class WeeklyExpensesUIState {
    object Loading : WeeklyExpensesUIState()

    object Success : WeeklyExpensesUIState()

    data class WeeklyExpenses(val expensesEntity: List<WeeklyTransactionsEntity>) : WeeklyExpensesUIState()

    data class Error(val statusCode: String, val message: String) : WeeklyExpensesUIState()
}

data class WeeklyTransactionsEntity(
    var expenseID: String,
    var expenseType: String,
    var expenseName: String,
    var expenseAmount: Float,
    var expenseAccount: String,
    var expenseCategory: String,
    var expenseDescription: String,
    var expenseImage: String,
    var expenseDate: String,
    var createdAt: String,
    var TotalExpense: String,
    var TotalIncome: String
)
