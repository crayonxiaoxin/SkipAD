package com.github.crayonxiaoxin.abc.ui.rules

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.base.BaseListFragment
import com.github.crayonxiaoxin.abc.databinding.FragmentListBinding
import com.github.crayonxiaoxin.abc.databinding.ItemKeywordBinding
import com.github.crayonxiaoxin.abc.databinding.ItemViewIdBinding
import com.github.crayonxiaoxin.abc.databinding.LayoutListBinding
import com.github.crayonxiaoxin.abc.db.Repository
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.model.ViewId
import com.github.crayonxiaoxin.abc.navigate
import com.github.crayonxiaoxin.abc.utils.hide
import com.github.crayonxiaoxin.abc.utils.show
import kotlinx.coroutines.launch

class ViewIdFragment :
    BaseListFragment<ViewId, ItemViewIdBinding, ViewIdAdapter, FragmentListBinding>() {

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

    override fun initAdapter(): ViewIdAdapter {
        return ViewIdAdapter()
    }

    override fun initData() {
        rootBinding.toolBar.toolBar.hide()
        lifecycleScope.launch {
            Repository.viewIdDao.getAllObx().observe(this@ViewIdFragment) {
                emptyView.show(it.isEmpty())
                adapter.submitList(it)
            }
        }
    }

    override fun refreshData() {

    }
}