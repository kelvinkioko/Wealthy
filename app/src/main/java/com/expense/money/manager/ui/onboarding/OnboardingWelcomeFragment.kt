package com.expense.money.manager.ui.onboarding

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.constants.PreferenceHandler
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
class OnboardingWelcomeFragment : Fragment(R.layout.onboarding_welcome) {

    private val binding by viewBinding(OnboardingWelcomeBinding::bind)

    private val viewModel: DefaultViewModel by viewModel()

    private lateinit var preferenceHandler: PreferenceHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceHandler = PreferenceHandler(requireActivity())

        setupClickListeners()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is DefaultUIState.Loading -> { }
            }
        }

        viewModel.validateSetup(preferenceHandler = preferenceHandler)

        binding.apply {
            loadingAnimation.setPadding(0, -90, -90, -120)
            appTitle.paintFlags = appTitle.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        if (viewModel.validateSetup(preferenceHandler = preferenceHandler)) {
            findNavController().navigate(OnboardingWelcomeFragmentDirections.toExpensesHostFragment(hasBottomNav = true))
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            onboardGetStarted.debouncedClick(lifecycleScope) {
                findNavController().navigate(OnboardingWelcomeFragmentDirections.toOnBoardingSetupFragment())
            }
        }
    }
}
