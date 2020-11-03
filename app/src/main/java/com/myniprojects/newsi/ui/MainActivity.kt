package com.myniprojects.newsi.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?)
    {
        //Set dark theme
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        //delegate.applyDayNight()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()



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