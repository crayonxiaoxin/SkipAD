package com.github.crayonxiaoxin.abc.ui.log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.crayonxiaoxin.abc.App
import com.github.crayonxiaoxin.abc.base.BaseListAdapter
import com.github.crayonxiaoxin.abc.databinding.ItemLogBinding
import com.github.crayonxiaoxin.abc.model.Log
import com.github.crayonxiaoxin.abc.utils._e
import com.github.crayonxiaoxin.abc.utils.getAppInfo

class LogAdapter : BaseListAdapter<Log, ItemLogBinding>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Log>() {
            override fun areItemsTheSame(oldItem: Log, newItem: Log): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Log, newItem: Log): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun setItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindItemViewHolder(position: Int, data: Log, holder: ViewHolder) {
        holder.binding.log = data
        holder.binding.ivLogo.setImageDrawable(data.logo(holder.itemView.context))
    }
}