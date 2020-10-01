package com.expense.money.manager.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.SettingsFragmentBinding
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.viewBinding

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val binding by viewBinding(SettingsFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            accountTypes.debouncedClick(lifecycleScope) {
                findNavController().navigate(SettingsFragmentDirections.toSettingsAccountTypesFragment())
            }
            currencyTypes.debouncedClick(lifecycleScope) {
                findNavController().navigate(SettingsFragmentDirections.toSettingsCurrencyFragment())
            }
            categoryTypes.debouncedClick(lifecycleScope) {
                findNavController().navigate(SettingsFragmentDirections.toExpenseCategoryFragment())
            }
            transactionTypes.debouncedClick(lifecycleScope) {
                findNavController().navigate(SettingsFragmentDirections.toTransactionTypesFragment())
            }
        }
    }
}
