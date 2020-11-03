package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.myniprojects.newsi.R
import com.myniprojects.newsi.adapters.NewsClickListener
import com.myniprojects.newsi.adapters.NewsRecyclerAdapter
import com.myniprojects.newsi.databinding.FragmentHomeBinding
import com.myniprojects.newsi.model.News
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.exhaustive
import com.myniprojects.newsi.utils.hideKeyboard
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        initView()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_home, menu)

        val menuItem = menu.findItem(R.id.itemSearch)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(p0: String?): Boolean
                {
                    Timber.d("onQueryTextSubmit")
                    hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean
                {
                    return true
                }

            }
        )

        menuItem.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener
            {
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean
                {
                    Timber.d("onMenuItemActionExpand")
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean
                {
                    Timber.d("onMenuItemActionCollapse")
                    return true
                }
            }
        )
    }

    private fun setupObservers()
    {
        viewModel.dataState.observe(viewLifecycleOwner, {
            when (it)
            {
                is DataState.Success ->
                {
                    Timber.d("Success")
                    newsRecyclerAdapter.submitList(it.data)
                    displayProgressBar(false)
                }
                is DataState.Error ->
                {
                    Timber.d("Error")
                    displayProgressBar(false)
                }
                is DataState.Loading ->
                {
                    Timber.d("Loading")
                    displayProgressBar(true)
                }
            }.exhaustive
        })
    }

    private fun initView()
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
        binding.recViewNews.adapter = newsRecyclerAdapter
    }

    private fun openNews(news: News)
    {
        Timber.d("Id opened: ${news.id}")
        findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
        viewModel.openNews(news)
    }

    private fun likeNews(news: News)
    {
        Timber.d("Id liked: ${news.id}")
        viewModel.likeNews(news.id)
    }

    private fun displayProgressBar(isDisplayed: Boolean)
    {
        binding.progBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

}





