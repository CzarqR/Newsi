package com.myniprojects.newsi.adapters.newsrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.NewsSeparatorItemBinding
import com.myniprojects.newsi.utils.getDateFormatted

class SeparatorViewHolder(
    private val binding: NewsSeparatorItemBinding
) : RecyclerView.ViewHolder(binding.root)
{


    fun bind(date: String)
    {
        binding.txtTitle.text = date.getDateFormatted(binding.root.context)
    }

    companion object
    {
        fun create(parent: ViewGroup): SeparatorViewHolder
        {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_separator_item, parent, false)
            val binding = NewsSeparatorItemBinding.bind(view)
            return SeparatorViewHolder(binding)
        }
    }
}