package com.money.budget.wealthy.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.SettingsFragmentBinding
import com.money.budget.wealthy.util.debouncedClick
import com.money.budget.wealthy.util.viewBinding

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
        }
    }
}
