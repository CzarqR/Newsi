package com.myniprojects.newsi.ui

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.RemoteInput
import androidx.core.view.iterator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.chip.Chip
import com.myniprojects.newsi.R
import com.myniprojects.newsi.databinding.ActivityMainBinding
import com.myniprojects.newsi.utils.Constants.HOT_NEWS
import com.myniprojects.newsi.utils.Constants.SEARCH_INPUT_KEY
import com.myniprojects.newsi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


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

        initNotification()
        initChips()

        collectSearchQuery()
    }

    private fun initNotification()
    {
        notificationManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            getSystemService(NotificationManager::class.java)
        }
        else
        {
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
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
                    binding.chipsQuickSearch.visibility = View.GONE
                }
                R.id.settingsFragment ->
                {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.chipsQuickSearch.visibility = View.GONE
                }
                R.id.homeFragment ->
                {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.chipsQuickSearch.visibility = View.VISIBLE
                }
                R.id.likedFragment ->
                {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.chipsQuickSearch.visibility = View.GONE
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
        // search keyword
        val intent = this.intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        remoteInput?.getCharSequence(SEARCH_INPUT_KEY)?.toString()?.let {
            Timber.d("Received search keyword $it")
            viewModel.submitQuery(it)
            return
        }

        // hot news
        getIntent()?.getStringExtra(HOT_NEWS.first)?.let {
            Timber.d("Received intent hot news search keyword $it")
            viewModel.submitQuery(it)
            return
        }
    }

    private fun collectSearchQuery()
    {
        lifecycleScope.launch {
            viewModel.queryFlow.collectLatest {

                if (it == null)
                {
                    binding.chipsQuickSearch.clearCheck()
                }
                else
                {
                    val submittedQuery = it.toLowerCase(Locale.getDefault())

                    for (v: View in binding.chipsQuickSearch.iterator())
                    {
                        val c = v as Chip
                        val chipText = c.text.toString().toLowerCase(Locale.getDefault())

                        if (chipText == submittedQuery)
                        {
                            c.isChecked = true
                            return@collectLatest
                        }
                    }
                    binding.chipsQuickSearch.clearCheck()
                }
            }
        }
    }


    private fun initChips()
    {
        viewModel.showHotNews.observe(this, {
            if (it)
            {
                val hotNews: Array<String> = resources.getStringArray(R.array.hot_news_keywords)

                with(binding.chipsQuickSearch)
                {

                    val inflater = LayoutInflater.from(context)

                    val children = hotNews.map { keyword ->
                        val chip = inflater.inflate(R.layout.hot_news_chip, this, false) as Chip
                        chip.text = keyword

                        chip.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked)
                            {
                                viewModel.submitQuery(keyword)
                            }
                            else //check if unselected
                            {
                                if (binding.chipsQuickSearch.checkedChipIds.size == 0)
                                {
                                    viewModel.submitQuery(null)
                                }
                            }
                        }
                        chip
                    }


                    val q = viewModel.queryFlow.value

                    removeAllViews()

                    if (q == null)
                    {
                        for (chip in children)
                        {
                            addView(chip)
                        }
                    }
                    else
                    {
                        val ql = q.toLowerCase(Locale.getDefault())
                        for (chip in children)
                        {
                            if (chip.text.toString().toLowerCase(Locale.ROOT) == ql)
                            {
                                chip.isChecked = true
                            }
                            addView(chip)
                        }
                    }
                }
            }
            else
            {
                binding.chipsQuickSearch.removeAllViews()
            }
        })


    }
}