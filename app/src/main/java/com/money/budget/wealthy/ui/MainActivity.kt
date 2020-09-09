package com.money.budget.wealthy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    fun setBottomNavigationVisibility(isVisible: Boolean) {
        binding.bottomNavigation.isVisible = isVisible
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.apply {
            setupWithNavController(findNavController(R.id.navHostFragment))

            findNavController(R.id.navHostFragment).addOnDestinationChangedListener { _, _, args ->
                // Top level items should have such argument with value set to true
                isVisible = (args?.getBoolean("hasBottomNav") == true)
            }
        }
    }
}
