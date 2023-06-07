package com.github.crayonxiaoxin.abc.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.base.BaseFragment
import com.github.crayonxiaoxin.abc.databinding.FragmentRulesBinding
import com.github.crayonxiaoxin.abc.navigate
import com.google.android.material.tabs.TabLayoutMediator

class RulesFragment : BaseFragment() {
    private lateinit var mediator: TabLayoutMediator
    private lateinit var binding: FragmentRulesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolBar.apply {
            toolBarTitle.text = getString(R.string.setting_rules)
            toolBarBack.setOnClickListener { navigate?.back() }
        }
        val tabs = arrayOf(getString(R.string.keyword), getString(R.string.viewid))
        val pagerAdapter = RulesPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.registerOnPageChangeCallback(changeCallback)
        mediator = TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = tabs[position]
        }
        mediator.attach()
    }

    private val changeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

        }
    }

    override fun onDestroy() {
        mediator.detach()
        binding.viewPager.unregisterOnPageChangeCallback(changeCallback)
        super.onDestroy()
    }
}