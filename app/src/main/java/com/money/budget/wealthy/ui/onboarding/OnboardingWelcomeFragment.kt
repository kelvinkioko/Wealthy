package com.money.budget.wealthy.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.money.budget.wealthy.R
import com.money.budget.wealthy.constants.setup.DefaultUIState
import com.money.budget.wealthy.constants.setup.DefaultViewModel
import com.money.budget.wealthy.databinding.OnboardingWelcomeBinding
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class OnboardingWelcomeFragment : Fragment(R.layout.onboarding_welcome) {

    private val binding by viewBinding(OnboardingWelcomeBinding::bind)

    private val viewModel: DefaultViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  create an adapter Object
        val adapter = WelcomePagerAdapter()
        binding.apply {
            //  Link adapter object to the pager
            welcomePager.adapter = adapter
            //  Link pager to indicator
            welcomeIndicator.setViewPager(welcomePager)
        }

        setupClickListeners()

//        Hive().loadWeeks()
//        Hive().getWeeksOfMonth()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is DefaultUIState.Loading -> { }
            }
        }

//        viewModel.setupCurrency()
    }

    private fun setupClickListeners() {
        binding.apply {
            onboardGetStarted.debouncedClick(lifecycleScope) {
                findNavController().navigate(OnboardingWelcomeFragmentDirections.toExpensesHostFragment())
            }
        }
    }
}
