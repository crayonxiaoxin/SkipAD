package com.github.crayonxiaoxin.abc.ui.rules

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.base.BaseFragment
import com.github.crayonxiaoxin.abc.databinding.FragmentRulesBinding
import com.github.crayonxiaoxin.abc.db.Repository
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.model.ViewId
import com.github.crayonxiaoxin.abc.navigate
import com.github.crayonxiaoxin.abc.utils.showDeleteDialog
import com.github.crayonxiaoxin.abc.utils.showKeywordDialog
import com.github.crayonxiaoxin.abc.utils.toast
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            toolBarRight.text = getString(R.string.add)
            registerForContextMenu(toolBarRight)
            toolBarRight.setOnClickListener {
                it.showContextMenu()
            }
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


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = MenuInflater(context)
        inflater.inflate(R.menu.menu_add_rule, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_keyword -> {
                addKeyword()
                true
            }

            R.id.menu_view_id -> {
                addViewId()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun addKeyword() {
        context.showKeywordDialog(
            hint = getString(R.string.keyword),
            value = ""
        ) {
            if (it.isEmpty()) {
                toast(getString(R.string.must_not_empty))
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    Repository.keywordDao.insertAll(Keyword(keyword = it))
                }
            }
        }
    }

    private fun addViewId() {
        context.showKeywordDialog(
            hint = getString(R.string.viewid),
            value = ""
        ) {
            if (it.isEmpty()) {
                toast(getString(R.string.must_not_empty))
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    Repository.viewIdDao.insertAll(ViewId(viewId = it))
                }
            }

        }
    }

}