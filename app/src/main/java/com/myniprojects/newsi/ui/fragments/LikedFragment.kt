package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.myniprojects.newsi.R
import com.myniprojects.newsi.adapters.NewsClickListener
import com.myniprojects.newsi.adapters.NewsLoadStateAdapter
import com.myniprojects.newsi.adapters.NewsRecyclerAdapter
import com.myniprojects.newsi.databinding.FragmentLikedBinding
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikedFragment : Fragment(R.layout.fragment_liked)
{
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentLikedBinding

    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLikedBinding.bind(view)

        initAdapter()
    }


    private fun initAdapter()
    {
        val newsClickListener = NewsClickListener(
            {
                openNews(it)
            },
            {
                likeNews(it)
            }
        )
        newsRecyclerAdapter = NewsRecyclerAdapter(newsClickListener)

        binding.recViewNews.adapter = newsRecyclerAdapter.withLoadStateHeaderAndFooter(
            header = NewsLoadStateAdapter { newsRecyclerAdapter.retry() },
            footer = NewsLoadStateAdapter { newsRecyclerAdapter.retry() }
        )

    }

    private fun likeNews(news: News)
    {
        TODO("Not yet implemented")
    }

    private fun openNews(news: News)
    {
        TODO("Not yet implemented")
    }

}