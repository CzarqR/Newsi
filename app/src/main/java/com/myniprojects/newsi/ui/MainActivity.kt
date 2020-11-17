package com.myniprojects.newsi.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.ActivityMainBinding
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        observeSharedPreferences()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupNavigation()
    }


    private fun observeSharedPreferences()
    {

        viewModel.darkMode.observe(this, {
            Timber.d("Observed Theme $it")
            if (it)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
            else
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
        })
    }

    private fun setupNavigation()
    {
        //set toolbar
        setSupportActionBar(binding.toolbar)

        // connect nav graph
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { /*to not reload fragment again*/ }

        // beck button
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)

        // change visibility in news fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id)
            {
                R.id.newsFragment ->
                {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.appBarLayout.setExpanded(true)
                }
                R.id.settingsFragment->
                {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.appBarLayout.setExpanded(true)

                }
                else ->
                {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean
    {
        return navController.navigateUp()
    }
}