package com.github.crayonxiaoxin.abc.ui.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.base.BaseListFragment
import com.github.crayonxiaoxin.abc.databinding.FragmentListBinding
import com.github.crayonxiaoxin.abc.databinding.ItemLogBinding
import com.github.crayonxiaoxin.abc.databinding.LayoutListBinding
import com.github.crayonxiaoxin.abc.db.Repository
import com.github.crayonxiaoxin.abc.model.Log
import com.github.crayonxiaoxin.abc.navigate
import com.github.crayonxiaoxin.abc.utils.show
import kotlinx.coroutines.launch

class LogFragment : BaseListFragment<Log, ItemLogBinding, LogAdapter, FragmentListBinding>() {

    override fun enableRefresh(): Boolean = false
    override fun enableLoadMore(): Boolean = false

    override fun setRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, container, false)
    }

    override fun setListBinding(): LayoutListBinding {
        return rootBinding.layoutList
    }

    override fun initAdapter(): LogAdapter {
        return LogAdapter()
    }

    override fun initData() {
        rootBinding.toolBar.apply {
            toolBarTitle.text = getString(R.string.setting_log)
            toolBarBack.setOnClickListener { navigate?.back() }
        }
        lifecycleScope.launch {
            Repository.logDao.getAllObx().observe(this@LogFragment) {
                emptyView.show(it.isEmpty())
                adapter.submitList(it)
            }
        }
    }

    override fun refreshData() {

    }
}