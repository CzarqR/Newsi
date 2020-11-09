package com.myniprojects.newsi.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.myniprojects.newsi.domain.News


class NewsRecyclerAdapter(
    private val newsClickListener: NewsClickListener
) : PagingDataAdapter<News, NewsViewHolder>(NewsDiffCallback)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
    {
        return NewsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int)
    {
        holder.bind(getItem(position)!!, newsClickListener)
    }

}


object NewsDiffCallback : DiffUtil.ItemCallback<News>()
{
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean
    {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean
    {
        return oldItem == newItem
    }
}

class NewsClickListener(
    val openClickListener: (news: News) -> Unit,
    val likeClickListener: (news: News) -> Unit
)
{
    fun openClick(news: News) = openClickListener(news)
    fun likeClick(news: News) = likeClickListener(news)
}
