package com.myniprojects.newsi.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.ActivityMainBinding
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.utils.logD
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var newsRetrofit: NewsRetrofit

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupNavigation()

        testCall()
    }

    private fun testCall()
    {
        newsRetrofit.getTrending(3).enqueue(
            object : Callback<String>
            {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                )
                {
                    response.body().logD()
                }

                override fun onFailure(
                    call: Call<String>,
                    t: Throwable
                )
                {
                    t.message.logD()
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