package com.expense.money.manager.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.SettingsFragmentBinding
import com.expense.money.manager.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val binding by viewBinding(SettingsFragmentBinding::bind)

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            accountTypes.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toSettingsAccountTypesFragment())
            }
            currencyTypes.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toSettingsCurrencyFragment())
            }
            categoryTypes.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toExpenseCategoryFragment())
            }
            transactionTypes.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toTransactionTypesFragment())
            }
            tellAFriend.setOnClickListener {
                viewModel.shareApp(requireActivity())
            }
            ratingReview.setOnClickListener {
                viewModel.rateApp(requireActivity())
            }
        }
    }
}
