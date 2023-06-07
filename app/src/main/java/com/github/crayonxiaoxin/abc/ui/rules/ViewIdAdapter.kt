package com.github.crayonxiaoxin.abc.ui.rules

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.crayonxiaoxin.abc.base.BaseListAdapter
import com.github.crayonxiaoxin.abc.databinding.ItemViewIdBinding
import com.github.crayonxiaoxin.abc.model.ViewId

class ViewIdAdapter : BaseListAdapter<ViewId, ItemViewIdBinding>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<ViewId>() {
            override fun areItemsTheSame(oldItem: ViewId, newItem: ViewId): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ViewId, newItem: ViewId): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun setItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewIdBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindItemViewHolder(position: Int, data: ViewId, holder: ViewHolder) {
        holder.binding.viewId = data
    }
}