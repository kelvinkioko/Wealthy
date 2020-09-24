package com.money.budget.wealthy.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.money.budget.wealthy.constants.Hive
import com.money.budget.wealthy.database.repository.AccountsRepository
import com.money.budget.wealthy.database.repository.CategoryRepository
import com.money.budget.wealthy.database.repository.ExpensesRepository
import com.money.budget.wealthy.database.repository.TransactionRepository
import com.money.budget.wealthy.util.Event
import java.util.Date

class StatisticsViewModel(
    private val accountsRepository: AccountsRepository,
    private val categoryRepository: CategoryRepository,
    private val expensesRepository: ExpensesRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<StatisticsUIState>()
    val uiState: LiveData<StatisticsUIState> = _uiState

    private val _action = MutableLiveData<Event<StatisticsActions>>()
    val action: LiveData<Event<StatisticsActions>> = _action

    init {
        loadTransactionTypes()
    }

    private fun loadTransactionTypes() {
        val transactionTypes = transactionRepository.loadTransactionTypes()
        val transactionList = mutableListOf<String>()
        for (transactionType in transactionTypes) {
            transactionList.add(transactionType.transactionName)
        }
        _uiState.postValue(StatisticsUIState.TransactionTypes(transactionList))
    }

    fun loadTransactions(transactionValue: String, durationValue: String, dateValue: String) {
        lateinit var startDate: Date
        lateinit var endDate: Date

        when (durationValue) {
            "Monthly" -> {
                startDate = Hive().getDateFromString("01/$dateValue")
                endDate = Hive().getDateFromString("31/$dateValue")
            }
            "Yearly" -> {
                startDate = Hive().getDateFromString("01/01/$dateValue")
                endDate = Hive().getDateFromString("31/12/$dateValue")
            }
            "Periodically" -> { }
        }

        val transactions = expensesRepository.loadExpensesByDateAndExpense(startDate, endDate, transactionValue)
    }
}

sealed class StatisticsActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : StatisticsActions()

    data class Navigate(val destination: NavDirections) : StatisticsActions()
}

sealed class StatisticsUIState {
    object Loading : StatisticsUIState()

    object Success : StatisticsUIState()

    data class TransactionTypes(val transactionTypesEntity: List<String>) : StatisticsUIState()
}
