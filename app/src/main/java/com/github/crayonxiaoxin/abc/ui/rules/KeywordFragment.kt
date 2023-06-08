package com.github.crayonxiaoxin.abc.ui.rules

import android.content.DialogInterface
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.base.BaseListFragment
import com.github.crayonxiaoxin.abc.databinding.DialogInputBinding
import com.github.crayonxiaoxin.abc.databinding.FragmentListBinding
import com.github.crayonxiaoxin.abc.databinding.ItemKeywordBinding
import com.github.crayonxiaoxin.abc.databinding.LayoutListBinding
import com.github.crayonxiaoxin.abc.db.Repository
import com.github.crayonxiaoxin.abc.model.Keyword
import com.github.crayonxiaoxin.abc.navigate
import com.github.crayonxiaoxin.abc.utils._e
import com.github.crayonxiaoxin.abc.utils.hide
import com.github.crayonxiaoxin.abc.utils.show
import com.github.crayonxiaoxin.abc.utils.showDeleteDialog
import com.github.crayonxiaoxin.abc.utils.showKeywordDialog
import com.github.crayonxiaoxin.abc.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeywordFragment :
    BaseListFragment<Keyword, ItemKeywordBinding, KeywordAdapter, FragmentListBinding>() {

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

    private var currentData: Keyword? = null

    override fun initAdapter(): KeywordAdapter {
        return KeywordAdapter().apply {
            setOnItemClickListener { _, data, _ ->
                currentData = data
                listBinding.recyclerView.showContextMenu()
            }
        }
    }

    override fun initData() {
        rootBinding.toolBar.toolBar.hide()
        registerForContextMenu(listBinding.recyclerView)
        lifecycleScope.launch {
            Repository.keywordDao.getAllObx().observe(this@KeywordFragment) {
                emptyView.show(it.isEmpty())
                adapter.submitList(it)
            }
        }
    }

    override fun refreshData() {

    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(1, 1, 1, R.string.update)
        menu.add(1, 2, 1, R.string.delete)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            1 -> {
                edit()
                true
            }

            2 -> {
                delete()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun edit() {
        context.showKeywordDialog(
            hint = getString(R.string.keyword),
            value = currentData?.keyword
        ) {
            if (it.isEmpty()) {
                toast(getString(R.string.must_not_empty))
            } else {
                currentData?.let { data ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        Repository.keywordDao.insertAll(data.copy(keyword = it))
                    }
                }
            }
        }
    }

    private fun delete() {
        context.showDeleteDialog {
            currentData?.let {
                lifecycleScope.launch(Dispatchers.IO) {
                    Repository.keywordDao.delete(it)
                }
            }
        }
    }
}