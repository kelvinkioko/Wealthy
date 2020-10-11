package com.expense.money.manager.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.constants.setup.DefaultUIState
import com.expense.money.manager.constants.setup.DefaultViewModel
import com.expense.money.manager.databinding.OnboardingWelcomeBinding
import com.expense.money.manager.util.debouncedClick
import com.expense.money.manager.util.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class OnboardingWelcomeFragment : Fragment(R.layout.onboarding_setup) {

    private val binding by viewBinding(OnboardingWelcomeBinding::bind)

    private val viewModel: DefaultViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is DefaultUIState.Loading -> { }
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            onboardGetStarted.debouncedClick(lifecycleScope) {
                findNavController().navigate(OnboardingWelcomeFragmentDirections.toExpensesHostFragment())
            }
        }
    }
}
