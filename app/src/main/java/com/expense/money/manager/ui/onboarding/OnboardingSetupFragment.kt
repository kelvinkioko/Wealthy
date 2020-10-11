package com.expense.money.manager.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.expense.money.manager.R
import com.expense.money.manager.constants.PreferenceHandler
import com.expense.money.manager.databinding.OnboardingSetupBinding
import com.expense.money.manager.util.observeEvent
import com.expense.money.manager.util.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
class OnboardingSetupFragment : Fragment(R.layout.onboarding_setup) {

    private val binding by viewBinding(OnboardingSetupBinding::bind)

    private val viewModel: OnboardingViewModel by viewModel()

    private lateinit var preferenceHandler: PreferenceHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceHandler = PreferenceHandler(requireActivity())

        setupData()
        setupClickListeners()
        viewModel.resetDataBasedOnPrefs()

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is OnboardingUIState.Loading -> { }
            }
        }

        viewModel.action.observeEvent(viewLifecycleOwner) {
            when (it) {
                is OnboardingActions.BottomNavigate -> showDialog(it.bottomSheetDialogFragment)
                is OnboardingActions.Navigate -> findNavController().navigate(it.destination)
            }
        }
    }

    private fun showDialog(bottomSheetDialogFragment: BottomSheetDialogFragment) {
        bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun setupData() {
        binding.apply {
            currencySetup.apply {
                actionNumber.text = "1"
                currencyTitle.text = "Set up your preferred currency"
                currencyDescription.text = "You can setup your preferred currency for all transaction types. This can be changed later from the settings page."
                setupActionButton.text = "Set currency"

                setupActionButton.setOnClickListener {
                    viewModel.currencySetup()
                    preferenceHandler.setCurrencySetup(currencySetup = true)
                }

                if (preferenceHandler.getCurrencySetup()!!) {
                    actionNumber.isGone = true
                    currencyRadio.apply {
                        isPressed = true
                        isChecked = true
                    }
                }
            }

            accountTypeSetup.apply {
                actionNumber.text = "2"
                currencyTitle.text = "Set up account types"
                currencyDescription.text = "You can have create different account types such as Cash, Bank. You will you use these types to organize your transaction accounts i.e banks under bank types. These can be changed later from the settings page."
                setupActionButton.text = "Set account types"

                setupActionButton.setOnClickListener {
                    viewModel.accountTypesSetup()
                    preferenceHandler.setAccountTypesSetup(accountTypesSetup = true)
                }

                if (preferenceHandler.getAccountTypesSetup()!!) {
                    actionNumber.isGone = true
                    currencyRadio.apply {
                        isPressed = true
                        isChecked = true
                    }
                }
            }

            accountSetup.apply {
                actionNumber.text = "3"
                currencyTitle.text = "Set up accounts"
                currencyDescription.text = "With the account types you have, you can properly organize your accounts such as bank accounts under bank type, physical cash reserves under cash types. These can be changed later from the settings page."
                setupActionButton.text = "Set accounts"

                setupActionButton.setOnClickListener {
                    viewModel.accountsSetup()
                    preferenceHandler.setAccountSetup(accountSetup = true)
                }

                if (preferenceHandler.getAccountSetup()!!) {
                    actionNumber.isGone = true
                    currencyRadio.apply {
                        isPressed = true
                        isChecked = true
                    }
                }
            }

            expenseTypesSetup.apply {
                actionNumber.text = "4"
                currencyTitle.text = "Set up expense types"
                currencyDescription.text = "Create all the categories you want to add your expenses under such as rent, transport, food e.t.c. These expense categories can also be added from the settings page."
                setupActionButton.text = "Set expense types"
                setupActionView.isGone = true

                setupActionButton.setOnClickListener {
                    viewModel.expenseTypesSetup()
                    preferenceHandler.setExpensesSetup(expensesSetup = true)
                }

                if (preferenceHandler.getExpensesSetup()!!) {
                    actionNumber.isGone = true
                    currencyRadio.apply {
                        isPressed = true
                        isChecked = true
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
//        binding.apply {
//            onboardGetStarted.debouncedClick(lifecycleScope) {
//                findNavController().navigate(OnboardingWelcomeFragmentDirections.toExpensesHostFragment())
//            }
//        }
    }
}
