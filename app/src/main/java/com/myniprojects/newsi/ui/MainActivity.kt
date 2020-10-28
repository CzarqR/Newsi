package com.myniprojects.newsi.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?)
    {
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

override fun onOptionsItemSelected(item: MenuItem): Boolean
{
    Log.d("MyTag", "Activity")
    return super.onOptionsItemSelected(item)
}

override fun onSupportNavigateUp(): Boolean
{
    Log.d("MyTag", "Up")
    return navController.navigateUp()
}
}