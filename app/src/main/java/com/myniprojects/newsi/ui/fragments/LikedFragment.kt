package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.myniprojects.newsi.R
import com.myniprojects.newsi.adapters.NewsClickListener
import com.myniprojects.newsi.adapters.NewsLoadStateAdapter
import com.myniprojects.newsi.adapters.NewsRecyclerAdapter
import com.myniprojects.newsi.databinding.FragmentLikedBinding
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.utils.openWeb
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
        setupObservers()
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

        lifecycleScope.launch(Dispatchers.IO){
            viewModel.likedNews.collectLatest {
                Timber.d("Submit data. ${viewModel.scrollPosLiked.value}")
                newsRecyclerAdapter.submitData(it)
            }
        }

        binding.recViewNews.adapter = newsRecyclerAdapter

//        binding.recViewNews.adapter = newsRecyclerAdapter.withLoadStateHeaderAndFooter(
//            header = NewsLoadStateAdapter { newsRecyclerAdapter.retry() },
//            footer = NewsLoadStateAdapter { newsRecyclerAdapter.retry() }
//        )

    }

    private fun likeNews(news: News)
    {

        Timber.d("Liked $news")
        viewModel.likeNews(news)
    }

    private fun openNews(news: News)
    {
        Timber.d("Opened $news")

        if (viewModel.openInExternal.value == true)
        {
            if (!openWeb(news.url))
            {
                Timber.d("Couldn't open web page")
            }
        }
        else
        {
            findNavController().navigate(R.id.action_likedFragment_to_newsFragment)
        }
    }

    private fun setupObservers()
    {
        viewModel.scrollPosLiked.observe(viewLifecycleOwner, {
            it?.let {
                Timber.d("Observed ${this.hashCode()} $it")
                binding.recViewNews.layoutManager?.onRestoreInstanceState(it)
            }
        })
    }


    override fun onStop()
    {
        Timber.d("Stop")
        super.onStop()
        // save recyclerView state
        viewModel.saveLikedScrollPosition(binding.recViewNews.layoutManager?.onSaveInstanceState())
    }
}