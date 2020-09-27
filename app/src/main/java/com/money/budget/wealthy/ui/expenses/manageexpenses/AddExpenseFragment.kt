package com.money.budget.wealthy.ui.expenses.manageexpenses

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.money.budget.wealthy.R
import com.money.budget.wealthy.database.models.AccountsEntity
import com.money.budget.wealthy.database.models.CategoryTypesEntity
import com.money.budget.wealthy.database.models.TransactionTypesEntity
import com.money.budget.wealthy.databinding.AddExpensesFragmentBinding
import com.money.budget.wealthy.ui.SharedViewModel
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.observeEvent
import com.money.budget.wealthy.util.viewBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale
import kotlinx.android.synthetic.main.add_expenses_fragment.accountAmount
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddExpenseFragment : Fragment(R.layout.add_expenses_fragment) {

    private val binding by viewBinding(AddExpensesFragmentBinding::bind)

    private val viewModel: ManageExpenseViewModel by viewModel()

    private lateinit var sharedViewModel: SharedViewModel

    private var transactionTypeSelected: Boolean = false

    private val transactionTypeAdapter: TransactionTypesAdapter by lazy {
        TransactionTypesAdapter { onTransactionTypePicked(it) }
    }

    private fun onTransactionTypePicked(transactionTypes: TransactionTypesEntity) {
        viewModel.setTransactionTypes(transactionTypes)
        transactionTypeSelected = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = requireActivity().run { ViewModelProvider(this).get(SharedViewModel::class.java) }

        setupToolbar()
        setupClickListener()
        setupObservers()
        setupLists()

        viewModel.loadTransactionTypes()
    }

    private fun setupToolbar() {
        binding.apply {
            toolbarTitle.text = "Add transaction"
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListener() {
        binding.apply {
            accountType.parent.debouncedClick(lifecycleScope) {
                viewModel.chooseAccount()
            }
            category.parent.debouncedClick(lifecycleScope) {
                viewModel.chooseCategory()
            }
            transactionDate.editText!!.debouncedClick(lifecycleScope) {
                // Date Range Picker
                val builder = MaterialDatePicker.Builder.datePicker()
                val picker = builder.build()
                picker.show(requireActivity().supportFragmentManager, picker.toString())
                picker.addOnCancelListener {
                    Log.d("DatePicker Activity", "Dialog was cancelled")
                }
                picker.addOnPositiveButtonClickListener {
                    val uniformDateFormatter = SimpleDateFormat("dd/MM/yyyy").format(it)
                    transactionDate.editText!!.setText(uniformDateFormatter)
                }
            }
            saveTransaction.debouncedClick(lifecycleScope) {
                when {
                    expenseNickname.editText!!.text.toString().isEmpty() -> expenseNickname.error = "Required"
                    accountAmount.editText!!.text.toString().isEmpty() -> accountAmount.error = "Required"
                    !transactionTypeSelected -> transactionTypesError.isVisible = true
                    accountType.accountTypeName.text.toString().contains("Select") -> accountTypeError.isVisible = true
                    category.accountTypeName.text.toString().contains("Select") -> categoryError.isVisible = true
                    transactionDate.editText!!.text.toString().isEmpty() -> transactionDate.error = "Required"
                    else -> {
                        viewModel.saveExpense(
                            name = expenseNickname.editText!!.text.toString(),
                            amount = accountAmount.editText!!.text.toString().replace("[Ksh,]".toRegex(), ""),
                            description = transactionDescription.editText!!.text.toString(),
                            date = transactionDate.editText!!.text.toString()
                        )
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is ManageExpenseUIState.Loading -> { }
                is ManageExpenseUIState.Success -> findNavController().navigateUp()
                is ManageExpenseUIState.Accounts -> renderAccounts(it.accountsEntity)
                is ManageExpenseUIState.CategoryTypes -> renderCategory(it.categoryTypesEntity)
                is ManageExpenseUIState.TransactionTypes -> renderTransactionTypes(it.transactionTypesEntity)
                is ManageExpenseUIState.Error -> { }
            }
        }
        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is ManageExpenseActions.Navigate -> findNavController().navigate(it.destination)
                is ManageExpenseActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
            }
        }
        sharedViewModel.reload.observe(viewLifecycleOwner, Observer {
            if (it.toString().toBoolean()) {
                viewModel.chooseAccount()
            }
        })
        var current = ""
        accountAmount.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stringText = s.toString()

                if (stringText != current && stringText.isNotEmpty() && !stringText.equals("Ksh ", true)) {
                    accountAmount.editText!!.removeTextChangedListener(this)

                    val locale = Locale.UK
                    val currency = Currency.getInstance(locale)
                    val cleanString = stringText.replace("[Ksh,.]".toRegex(), "")
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100)
                    val secondCleanString = formatted.replace("[${currency.symbol}]".toRegex(), "Ksh ")

                    current = secondCleanString
                    accountAmount.editText!!.setText(secondCleanString)
                    accountAmount.editText!!.setSelection(secondCleanString.length)
                    accountAmount.editText!!.addTextChangedListener(this)
                }
            }
        })
    }

    private fun renderAccounts(accounts: AccountsEntity) {
        binding.apply {
            accountType.apply {
                accountTypeName.text = accounts.sourceName
                accountTypeDescription.text = accounts.sourceDescription
                accountTypeDescription.isVisible = true
            }
        }
    }

    private fun renderCategory(categoryType: CategoryTypesEntity) {
        binding.apply {
            category.apply {
                accountTypeName.text = categoryType.categoryName
                accountTypeDescription.text = categoryType.categoryDescription
                accountTypeDescription.isVisible = true
            }
        }
    }

    private fun renderTransactionTypes(transactionTypes: List<TransactionTypesEntity>) {
        transactionTypeAdapter.setTransactionTypes(transactionType = transactionTypes)
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun setupLists() {
        binding.apply {
            transactionTypes.adapter = transactionTypeAdapter
        }
    }
}
