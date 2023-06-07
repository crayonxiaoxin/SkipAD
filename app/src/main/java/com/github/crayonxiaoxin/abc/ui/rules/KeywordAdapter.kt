package com.github.crayonxiaoxin.abc.ui.rules

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.crayonxiaoxin.abc.base.BaseListAdapter
import com.github.crayonxiaoxin.abc.databinding.ItemKeywordBinding
import com.github.crayonxiaoxin.abc.model.Keyword

class KeywordAdapter : BaseListAdapter<Keyword, ItemKeywordBinding>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Keyword>() {
            override fun areItemsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Keyword, newItem: Keyword): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun setItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemKeywordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindItemViewHolder(position: Int, data: Keyword, holder: ViewHolder) {
        holder.binding.keyword = data
    }
}