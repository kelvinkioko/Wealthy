package com.expense.money.manager.ui.settings.transactiontypes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.database.models.TransactionTypesEntity
import com.expense.money.manager.databinding.SettingsTransactionTypesFragmentBinding
import com.expense.money.manager.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionTypesFragment : Fragment(R.layout.settings_transaction_types_fragment) {

    private val binding by viewBinding(SettingsTransactionTypesFragmentBinding::bind)

    private val viewModel: TransactionTypesViewModel by viewModel()

    private val transactionTypesAdapter: TransactionTypesAdapter by lazy {
        TransactionTypesAdapter { onTransactionTypesPicked(it) }
    }

    private fun onTransactionTypesPicked(transactionTypesEntity: TransactionTypesEntity) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupViewObservers()
        setupTransactionTypesList()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupViewObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is TransactionTypesUIState.TransactionTypes -> populateTransactionTypes(it.transactionTypesEntity)
            }
        }
    }

    private fun populateTransactionTypes(transactionTypesEntity: List<TransactionTypesEntity>) {
        transactionTypesAdapter.setTransactionTypes(transactionTypesEntity)
    }

    private fun setupTransactionTypesList() {
        binding.transactionTypesList.adapter = transactionTypesAdapter
    }
}
