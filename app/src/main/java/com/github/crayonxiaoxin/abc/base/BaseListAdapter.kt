package com.github.crayonxiaoxin.abc.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * 列表基類
 * Created by Lau on 2020-03-03.
 */
abstract class BaseListAdapter<ItemBean, ItemBinding : ViewDataBinding>(diff: DiffUtil.ItemCallback<ItemBean>) :
    ListAdapter<ItemBean, BaseListAdapter<ItemBean, ItemBinding>.ViewHolder>(diff) {

    private var onItemClick: ((position: Int, data: ItemBean, holder: ViewHolder) -> Unit)? = null

    inner class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = setItemViewHolder(parent, viewType)
        // 防止ViewPager缓存导致的crash问题
        // ViewPager切换不缓存的页面时不走完整的生命周期，导致adapter重复赋值，此时adapter持有的ViewHolder.itemView已经有parent了???
        holder.itemView.parent?.let {
            (it as ViewGroup).removeView(holder.itemView)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick?.let {
                it(position, data, holder)
            }
        }
        bindItemViewHolder(position, data, holder)
    }

    fun setOnItemClickListener(listener: ((position: Int, data: ItemBean, holder: ViewHolder) -> Unit)?) {
        this.onItemClick = listener
    }

    // 设置item布局
    abstract fun setItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder

    // 设置item数据
    abstract fun bindItemViewHolder(position: Int, data: ItemBean, holder: ViewHolder)

}