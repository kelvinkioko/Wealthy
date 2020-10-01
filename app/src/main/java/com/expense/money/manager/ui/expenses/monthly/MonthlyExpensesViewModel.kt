package com.expense.money.manager.ui.expenses.monthly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.expense.money.manager.constants.Hive
import com.expense.money.manager.database.repository.ExpensesRepository
import com.expense.money.manager.util.Event
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MonthlyExpensesViewModel(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<MonthlyExpensesUIState>()
    val uiState: LiveData<MonthlyExpensesUIState> = _uiState

    private val _action = MutableLiveData<Event<MonthlyExpensesActions>>()
    val action: LiveData<Event<MonthlyExpensesActions>> = _action

    private val months: Array<String> = arrayOf("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December")

    fun loadExpenses(year: String) {
        val monthlyTransactionsItem = mutableListOf<MonthlyTransactionsEntity>()

        for (i in 0..11) {
            val startDate = "01/${i + 1}/$year"
            val endDate = "31/${i + 1}/$year"
            monthlyTransactionsItem.add(
                MonthlyTransactionsEntity(
                    MonthName = months[i],
                    MonthPosition = "${i + 1}",
                    Transactions = expensesRepository.countExpensesByRange(Hive().getDateFromString(startDate), Hive().getDateFromString(endDate)).toString() + " Transactions",
                    TotalExpense = expensesRepository.loadAnnualExpensesByDate(Hive().getDateFromString(startDate), Hive().getDateFromString(endDate)).toString(),
                    TotalIncome = expensesRepository.loadAnnualIncomeByDate(Hive().getDateFromString(startDate), Hive().getDateFromString(endDate)).toString()
                )
            )
        }

        _uiState.postValue(MonthlyExpensesUIState.MonthlyExpenses(monthlyTransactionsItem))
    }
}

sealed class MonthlyExpensesActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : MonthlyExpensesActions()

    data class Navigate(val destination: NavDirections) : MonthlyExpensesActions()
}

sealed class MonthlyExpensesUIState {
    object Loading : MonthlyExpensesUIState()

    object Success : MonthlyExpensesUIState()

    data class MonthlyExpenses(val expensesEntity: List<MonthlyTransactionsEntity>) : MonthlyExpensesUIState()

    data class Error(val statusCode: String, val message: String) : MonthlyExpensesUIState()
}

data class MonthlyTransactionsEntity(
    val MonthName: String,
    val MonthPosition: String,
    val Transactions: String,
    var TotalExpense: String,
    var TotalIncome: String
)
