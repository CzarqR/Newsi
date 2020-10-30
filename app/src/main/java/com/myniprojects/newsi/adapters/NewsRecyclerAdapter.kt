package com.myniprojects.newsi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myniprojects.newsi.databinding.NewsRecyclerItemBinding
import com.myniprojects.newsi.model.News
import javax.inject.Inject


class NewsRecyclerAdapter(
    private val newsClickListener: NewsClickListener
) : ListAdapter<News, NewsRecyclerAdapter.ViewHolder>(NewsDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(getItem(position)!!, newsClickListener)
    }

    class ViewHolder private constructor(
        private val binding: NewsRecyclerItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
    {
        companion object
        {
            fun from(parent: ViewGroup): ViewHolder
            {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NewsRecyclerItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
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

}


class NewsDiffCallback @Inject constructor() : DiffUtil.ItemCallback<News>()
{
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean
    {
        return oldItem.id == newItem.id
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
