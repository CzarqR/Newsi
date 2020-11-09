package com.myniprojects.newsi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.NewsLoadStateFooterBinding

class NewsLoadStateViewHolder(
    private val binding: NewsLoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root)
{

    init
    {
        binding.butRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState)
    {
        if (loadState is LoadState.Error)
        {
            binding.txtErrorMsg.text = loadState.error.localizedMessage
        }
        binding.proBar.isVisible = loadState is LoadState.Loading
        binding.butRetry.isVisible = loadState !is LoadState.Loading
        binding.txtErrorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object
    {
        fun create(parent: ViewGroup, retry: () -> Unit): NewsLoadStateViewHolder
        {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_load_state_footer, parent, false)
            val binding = NewsLoadStateFooterBinding.bind(view)
            return NewsLoadStateViewHolder(binding, retry)
        }
    }
}