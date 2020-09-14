package com.money.budget.wealthy.ui.expenses.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.util.Event

class DailyExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<DailyExpensesUIState>()
    val uiState: LiveData<DailyExpensesUIState> = _uiState

    private val _action = MutableLiveData<Event<DailyExpensesActions>>()
    val action: LiveData<Event<DailyExpensesActions>> = _action

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        val transactions = expensesRepository.loadExpenses()
        val displayTransactionsEntity: ArrayList<DisplayTransactionsEntity> = ArrayList()
        for (transaction in transactions) {
            displayTransactionsEntity.add(
                DisplayTransactionsEntity(
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
        _uiState.postValue(DailyExpensesUIState.DailyExpenses(displayTransactionsEntity))
    }
}

sealed class DailyExpensesActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : DailyExpensesActions()

    data class Navigate(val destination: NavDirections) : DailyExpensesActions()
}

sealed class DailyExpensesUIState {
    object Loading : DailyExpensesUIState()

    object Success : DailyExpensesUIState()

    data class DailyExpenses(val expensesEntity: List<DisplayTransactionsEntity>) : DailyExpensesUIState()

    data class Error(val statusCode: String, val message: String) : DailyExpensesUIState()
}

data class DisplayTransactionsEntity(
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
