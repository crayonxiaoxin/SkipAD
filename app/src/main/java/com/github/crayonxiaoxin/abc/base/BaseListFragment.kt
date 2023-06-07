package com.github.crayonxiaoxin.abc.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.databinding.LayoutListBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class BaseListFragment<T : Any, ItemBinding : ViewDataBinding, Adapter : BaseListAdapter<T, ItemBinding>, Binding : ViewDataBinding> :
    BaseFragment() {

    protected lateinit var refreshLayout: SmartRefreshLayout
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var emptyView: TextView
    protected lateinit var frameLayout: FrameLayout

    protected lateinit var rootBinding: Binding
    protected lateinit var listBinding: LayoutListBinding
    protected lateinit var adapter: Adapter

    open fun enableRefresh(): Boolean = true
    open fun enableLoadMore(): Boolean = true
    open fun enableListDivider(): Boolean = true

    abstract fun setRootBinding(inflater: LayoutInflater, container: ViewGroup?): Binding
    abstract fun setListBinding(): LayoutListBinding
    abstract fun initAdapter(): Adapter

    abstract fun initData()
    abstract fun refreshData()
    open fun loadMoreData() {}

//    // 通过反射实例化 viewModel
//    private fun initViewModel() {
//        val genericSuperclass = javaClass.genericSuperclass as ParameterizedType
//        val actualTypeArguments = genericSuperclass.actualTypeArguments
//        if (actualTypeArguments.size > 1) {
//            val clazz = (actualTypeArguments[1] as Class<*>).asSubclass(AbsViewModel::class.java)
//            viewModel = ViewModelProvider(this)[clazz] as VM
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootBinding = setRootBinding(inflater, container)
        listBinding = setListBinding()
        recyclerView = listBinding.recyclerView
        refreshLayout = listBinding.refreshLayout.apply {
            setEnableRefresh(enableRefresh())
            setEnableLoadMore(enableLoadMore())
        }
        emptyView = listBinding.emptyView.apply {
            setText(context.getString(R.string.no_data))
        }
        frameLayout = listBinding.frameLayout

        adapter = initAdapter()

        setLayoutManager()
        setAdapter()
        recyclerView.itemAnimator = null

        // 分割线
        if (enableListDivider()) {
            ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)?.let {
                val dividerItemDecoration =
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                dividerItemDecoration.setDrawable(it)
                recyclerView.addItemDecoration(dividerItemDecoration)
            }
        }

        refreshLayout.setOnRefreshListener { refreshData() }
        refreshLayout.setOnLoadMoreListener { loadMoreData() }

        return rootBinding.root
    }


    open fun setLayoutManager() {
        recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    open fun setAdapter() {
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    open fun refreshIdle() {
        if (refreshLayout.isRefreshing) {
            refreshLayout.finishRefresh()
        }
        if (refreshLayout.isLoading) {
            refreshLayout.finishLoadMore()
        }
    }
}