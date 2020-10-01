package com.expense.money.manager.ui.settings.transactiontypes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.database.repository.TransactionRepository

class TransactionTypesViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<TransactionTypesUIState>()
    val uiState: LiveData<TransactionTypesUIState> = _uiState

    init {
        loadTransactionTypes()
    }

    private fun loadTransactionTypes() {
        val transactionTypes = transactionRepository.loadTransactionTypes()
        _uiState.postValue(TransactionTypesUIState.TransactionTypes(transactionTypes))
    }
}

sealed class TransactionTypesUIState {
    object Loading : TransactionTypesUIState()

    object Success : TransactionTypesUIState()

    data class TransactionTypes(val transactionTypesEntity: List<TransactionTypesEntity>) : TransactionTypesUIState()
}
