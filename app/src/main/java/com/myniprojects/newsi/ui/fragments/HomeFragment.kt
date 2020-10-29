package com.myniprojects.newsi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.FragmentHomeBinding
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.exhaustive
import com.myniprojects.newsi.utils.hideKeyboard
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)
{
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

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

        binding.butNews.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_newsFragment)
        )

        setupObservers()
        viewModel.loadTrendingNews()
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
                is DataState.Success -> Timber.d("Success")
                is DataState.Error -> Timber.d("Error")
                is DataState.Loading -> Timber.d("Loading")
            }.exhaustive
        })
    }
}





