package com.expense.money.manager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.expense.money.manager.R
import com.expense.money.manager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.apply {
            setupWithNavController(findNavController(R.id.navHostFragment))

            findNavController(R.id.navHostFragment).addOnDestinationChangedListener { _, _, args ->
                // Top level items should have such argument with value set to true
                isVisible = (args?.getBoolean("hasBottomNav") == true) || (args?.getBoolean("hasBottomNav") == null)
            }
        }
    }
}
