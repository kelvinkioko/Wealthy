package com.money.budget.wealthy.ui.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.money.budget.wealthy.R
import com.money.budget.wealthy.databinding.StatisticsFragmentBinding
import com.money.budget.wealthy.util.viewBinding

class StatisticsFragment : Fragment(R.layout.statistics_fragment) {

    private val binding by viewBinding(StatisticsFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
