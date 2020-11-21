package com.myniprojects.newsi.ui

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.RemoteInput
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.ActivityMainBinding
import com.myniprojects.newsi.utils.Constants.FRESH_NEWS_NOTIFICATION_ID
import com.myniprojects.newsi.utils.Constants.SEARCH_INPUT_KEY
import com.myniprojects.newsi.utils.createFreshNewsNotification
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var notificationManager: NotificationManager? = null

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Newsi)
        observeSharedPreferences()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveInput()

        setupNavigation()

        notificationManager?.cancelAll() // clear all notification
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
            binding.appBarLayout.setExpanded(true)
            when (destination.id)
            {
                R.id.newsFragment ->
                {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.settingsFragment ->
                {
                    binding.bottomNavigationView.visibility = View.VISIBLE

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

    private fun receiveInput()
    {
        val intent = this.intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        viewModel.submittedKey = remoteInput?.getCharSequence(SEARCH_INPUT_KEY)?.toString()
        notificationManager?.cancelAll()
    }
}