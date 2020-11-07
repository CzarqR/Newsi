package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.myniprojects.newsi.R
import com.myniprojects.newsi.adapters.NewsClickListener
import com.myniprojects.newsi.adapters.NewsRecyclerAdapter
import com.myniprojects.newsi.databinding.FragmentHomeBinding
import com.myniprojects.newsi.domain.News
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
        Timber.d("onCreateView")
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        Timber.d("onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        initView()
        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        Timber.d("onCreateOptionsMenu")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_home, menu)

        // region SearchView setup

        val menuItemSearch = menu.findItem(R.id.itemSearch)
        val searchView = menuItemSearch.actionView as SearchView

        // change searchText if it has been set
        viewModel.searchText?.let {
            menuItemSearch.expandActionView()
            searchView.setQuery(it, false)
            searchView.clearFocus()
        }


        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(p0: String?): Boolean
                {
                    Timber.d("onQueryTextSubmit")
                    hideKeyboard()
                    viewModel.searchText = p0
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean
                {
                    return true
                }

            }
        )

        menuItemSearch.setOnActionExpandListener(
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
                    viewModel.searchText = null
                    return true
                }
            }
        )

        // endregion

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.itemRefresh ->
            {
                Timber.d("Refresh")
                viewModel.loadMore()
                true
            }
            else ->
            {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun setupObservers()
    {
        viewModel.loadedNews.observe(viewLifecycleOwner, {
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
                    Snackbar.make(binding.root, R.string.couldnt_load_data, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.ok) {
                            // close snackbar
                        }
                        .show()
                    newsRecyclerAdapter.submitList(it.data)
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
//        Timber.d("Id opened: ${news.id}")
        findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
        viewModel.openNews(news)
    }

    private fun likeNews(news: News)
    {
        Timber.d("Id liked: ${news.url}")
//
//        viewModel.likeNews(news.id)
    }

    private fun displayProgressBar(isDisplayed: Boolean)
    {
        binding.progBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }
}





