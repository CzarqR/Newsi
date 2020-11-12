package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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
import com.myniprojects.newsi.databinding.FragmentHomeBinding
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.utils.hideKeyboard
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    private var isLoading = false // loading new news


    private var searchJob: Job? = null

    private fun search()
    {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchNews().collectLatest {
                newsRecyclerAdapter.submitData(it)
            }
        }
    }

    private fun initSearch()
    {
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            newsRecyclerAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recViewNews.scrollToPosition(0) }
        }
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

        newsRecyclerAdapter.addLoadStateListener { loadState ->

            if (newsRecyclerAdapter.itemCount == 0)
            {
                // todo better loading and showing no connection
                binding.recViewNews.isVisible = loadState.source.refresh is LoadState.NotLoading
                binding.proBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.butRetry.isVisible = loadState.source.refresh is LoadState.Error
            }
            else
            {
                binding.recViewNews.isVisible = true
                binding.proBar.isVisible = false
                binding.butRetry.isVisible = loadState.source.refresh is LoadState.Error
            }

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Snackbar.make(binding.root, R.string.couldnt_load_data, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.ok) {
                        // close snackbar
                    }
                    .show()
            }
        }


    }


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

        initAdapter()
        search()
        initSearch()

        binding.butRetry.setOnClickListener { newsRecyclerAdapter.retry() }
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
//                viewModel.refresh()
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


//        viewModel.loadedNews.observe(viewLifecycleOwner, {
//            when (it)
//            {
//                is DataState.Success ->
//                {
//                    Timber.d("Success")
//                    Timber.d("Size ${it.data.size}")
//                    newsRecyclerAdapter.submitList(it.data)
//                    displayProgressBar(false)
//                    isLoading = false
//                }
//                is DataState.Error ->
//                {
//                    Timber.d("Error")
//                    Snackbar.make(binding.root, R.string.couldnt_load_data, Snackbar.LENGTH_SHORT)
//                        .setAction(R.string.ok) {
//                            // close snackbar
//                        }
//                        .show()
//                    newsRecyclerAdapter.submitList(it.data)
//                    displayProgressBar(false)
//                    isLoading = false
//                }
//                is DataState.Loading ->
//                {
//                    Timber.d("Loading")
//                    displayProgressBar(true)
//                }
//            }.exhaustive
//        })
    }

    private fun initView()
    {
//        binding.recViewNews.addOnScrollListener(
//            object : RecyclerView.OnScrollListener()
//            {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
//                {
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    Timber.d("${(binding.recViewNews.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()} - ${newsRecyclerAdapter.itemCount - 1}")
//
//                    if (
//                        !isLoading &&
//                        (binding.recViewNews.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == newsRecyclerAdapter.itemCount - 1
//                    )
//                    {
//                        viewModel.loadTrendingNews()
//                        isLoading = true
//                    }
//                }
//            }
//        )
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

//    private fun displayProgressBar(isDisplayed: Boolean)
//    {
//        //binding.progBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
//    }
}





