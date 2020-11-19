package com.myniprojects.newsi.adapters.newsrecycler

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.myniprojects.newsi.R
import com.myniprojects.newsi.domain.News


class NewsRecyclerAdapter(
    private val newsClickListener: NewsClickListener
) : PagingDataAdapter<NewsRecyclerModel, RecyclerView.ViewHolder>(NewsDiffCallback)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {

        return when (viewType)
        {
            R.layout.news_recycler_item ->
            {
                NewsViewHolder.from(parent)
            }
            R.layout.news_separator_item ->
            {
                SeparatorViewHolder.create(parent)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int
    {
        return when (getItem(position))
        {
            is NewsRecyclerModel.NewsItem -> R.layout.news_recycler_item
            is NewsRecyclerModel.SeparatorItem -> R.layout.news_separator_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        val newsRecyclerModel = getItem(position)
        newsRecyclerModel.let {
            when (newsRecyclerModel)
            {
                is NewsRecyclerModel.NewsItem -> (holder as NewsViewHolder).bind(
                    newsRecyclerModel.news,
                    newsClickListener
                )
                is NewsRecyclerModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(
                    newsRecyclerModel.day
                )
            }
        }
    }
}


object NewsDiffCallback : DiffUtil.ItemCallback<NewsRecyclerModel>()
{
    override fun areItemsTheSame(oldItem: NewsRecyclerModel, newItem: NewsRecyclerModel): Boolean =
        (oldItem is NewsRecyclerModel.NewsItem && newItem is NewsRecyclerModel.NewsItem &&
                oldItem.news.url == newItem.news.url) ||
                (oldItem is NewsRecyclerModel.SeparatorItem && newItem is NewsRecyclerModel.SeparatorItem &&
                        oldItem.day == newItem.day)


    override fun areContentsTheSame(
        oldItem: NewsRecyclerModel,
        newItem: NewsRecyclerModel
    ): Boolean =
        oldItem == newItem


}

class NewsClickListener(
    val openClickListener: (news: News) -> Unit,
    val likeClickListener: (news: News) -> Unit
)
{
    fun openClick(news: News) = openClickListener(news)
    fun likeClick(news: News) = likeClickListener(news)
}
