package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.myniprojects.newsi.R
import com.myniprojects.newsi.adapters.newsrecycler.NewsClickListener
import com.myniprojects.newsi.adapters.newsrecycler.NewsLoadStateAdapter
import com.myniprojects.newsi.adapters.newsrecycler.NewsRecyclerAdapter
import com.myniprojects.newsi.databinding.FragmentHomeBinding
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.utils.hideKeyboard
import com.myniprojects.newsi.utils.openWeb
import com.myniprojects.newsi.utils.showSnackbarWithCancellation
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    private lateinit var newsRecyclerAdapter: NewsRecyclerAdapter


    private var menuItemSearch: MenuItem? = null
    private var searchView: SearchView? = null

    private var searchJob: Job? = null


//      Not used, it swipe recView all the time to top
//    private fun initSearch()
//    {
//        Timber.d("Init search")
//
//        // Scroll to top when the list is refreshed from network.
//        lifecycleScope.launch {
//            newsRecyclerAdapter.loadStateFlow
//                // Only emit when REFRESH LoadState for RemoteMediator changes.
//                .distinctUntilChangedBy { it.refresh }
//                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
//                .filter { it.refresh is LoadState.NotLoading }
//                .collect {
//                    binding.recViewNews.scrollToPosition(0)
//                }
//        }
//    }


    private fun setupObservers()
    {
        viewModel.scrollPosHome.observe(viewLifecycleOwner, {
            it?.let {
                Timber.d("Restore pos $it")
                binding.recViewNews.layoutManager?.onRestoreInstanceState(it)
            }
        })

        viewModel.countHomeNews.observe(viewLifecycleOwner, {
            it?.let {
                lifecycleScope.launch {
                    newsRecyclerAdapter.loadStateFlow.collectLatest { loadSate ->
                        withContext(Dispatchers.Main)
                        {
                            when (loadSate.mediator?.refresh)
                            {
                                is LoadState.NotLoading ->
                                {
                                    binding.txtNotFound.isVisible = it == 0L
                                }
                                LoadState.Loading, is LoadState.Error ->
                                {
                                    binding.txtNotFound.isVisible = false
                                }
                            }
                        }

                    }
                }
                binding.txtNotFound.isVisible = it <= 0
            }
        })
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

            with(binding)
            {
                when (loadState.refresh)
                {
                    is LoadState.NotLoading ->
                    {
                        recViewNews.isVisible = true
                        butRetry.isVisible = false
                        proBar.isVisible = false
                    }
                    LoadState.Loading ->
                    {
                        recViewNews.isVisible = false
                        butRetry.isVisible = false
                        proBar.isVisible = true
                    }
                    is LoadState.Error ->
                    {
                        proBar.isVisible = false

                        recViewNews.isVisible = false
                        butRetry.isVisible = true
                        if (newsRecyclerAdapter.itemCount > 0)
                        {
                            recViewNews.isVisible = true
                            butRetry.isVisible = false

                            binding.root.showSnackbarWithCancellation(R.string.couldnt_load_new_data)
                        }
                        else
                        {
                            recViewNews.isVisible = false
                            butRetry.isVisible = true

                            binding.root.showSnackbarWithCancellation(R.string.couldnt_load_data)
                        }
                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        Timber.d("onCreateView ${this.hashCode()}")
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        Timber.d("onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        initAdapter()

        collectNews()

        binding.butRetry.setOnClickListener { newsRecyclerAdapter.retry() }

        setupObservers()
    }


    private fun collectNews()
    {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.homeNewsData.collectLatest {
                newsRecyclerAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.queryFlow.collectLatest {

                menuItemSearch?.let { mi ->
                    searchView?.let { sv ->

                        Timber.d("$it")

                        if (it != null)
                        {
                            mi.expandActionView()
                            sv.setQuery(it, false)
                            sv.clearFocus()
                        }
                        else
                        {
                            requireActivity().findViewById<Toolbar>(R.id.toolbar)
                                .collapseActionView()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        Timber.d("onCreateOptionsMenu")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_home, menu)

        // region SearchView setup

        menuItemSearch = menu.findItem(R.id.itemSearch)
        searchView = menuItemSearch!!.actionView as SearchView

        // change searchText if it has been set
        viewModel.queryFlow.value?.let {
            menuItemSearch!!.expandActionView()
            searchView!!.setQuery(it, false)
            searchView!!.clearFocus()
        }

        searchView!!.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(textInput: String?): Boolean
                {
                    Timber.d("onQueryTextSubmit $textInput")
                    hideKeyboard()
                    viewModel.submitQuery(textInput)
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean
                {
                    return true
                }

            }
        )

        menuItemSearch!!.setOnActionExpandListener(
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
                    viewModel.submitQuery(null)
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

                binding.recViewNews.smoothScrollToPosition(0)
                newsRecyclerAdapter.refresh()
                true
            }
            else ->
            {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun openNews(news: News)
    {
        Timber.d("Id opened: ${news.url}")
        Timber.d(viewModel.openInExternal.value.toString())
        viewModel.openNews(news)

        if (viewModel.openInExternalValue())
        {
            if (!openWeb(news.url))
            {
                Timber.d("Couldn't open web page")
            }
        }
        else
        {
            findNavController().navigate(R.id.action_homeFragment_to_newsFragment)
        }
    }


    private fun likeNews(news: News)
    {
        Timber.d("Id liked: ${news.url}")
        viewModel.likeNews(news)
    }

    override fun onStop()
    {
        super.onStop()
        // save recyclerView state
        viewModel.saveHomeScrollPosition(binding.recViewNews.layoutManager?.onSaveInstanceState())
    }
}




