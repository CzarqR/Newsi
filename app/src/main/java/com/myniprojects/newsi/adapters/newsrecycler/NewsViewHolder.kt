package com.myniprojects.newsi.adapters.newsrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myniprojects.newsi.databinding.NewsRecyclerItemBinding
import com.myniprojects.newsi.domain.News

class NewsViewHolder private constructor(
    private val binding: NewsRecyclerItemBinding
) : RecyclerView.ViewHolder(binding.root)
{
    companion object
    {
        fun from(parent: ViewGroup): NewsViewHolder
        {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = NewsRecyclerItemBinding.inflate(layoutInflater, parent, false)
            return NewsViewHolder(
                binding
            )
        }
    }

    fun bind(
        news: News,
        newsClickListener: NewsClickListener
    )
    {
        binding.news = news
        binding.newsClickListener = newsClickListener
        binding.executePendingBindings()
    }

}