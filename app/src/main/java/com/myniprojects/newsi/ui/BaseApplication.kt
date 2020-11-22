package com.myniprojects.newsi.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.myniprojects.newsi.BuildConfig
import com.myniprojects.newsi.R
import com.myniprojects.newsi.utils.Constants.CHANNEL_FRESH_NEWS_ID
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.FIRST_RUN_SH
import com.myniprojects.newsi.utils.Constants.LAST_RUN_SH
import com.myniprojects.newsi.utils.Constants.LOAD_DEFAULT_TIME
import com.myniprojects.newsi.utils.Constants.REFRESH_WORK_NAME
import com.myniprojects.newsi.wrok.RefreshDataWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class BaseApplication : Application(), androidx.work.Configuration.Provider
{
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate()
    {
        super.onCreate()

        if (BuildConfig.DEBUG)
        {
            Timber.plant(Timber.DebugTree())
        }

        checkFirstRun()
        delayedInit()
    }


    private fun checkFirstRun()
    {
        val firstRun = sharedPreferences.getBoolean(FIRST_RUN_SH.first, FIRST_RUN_SH.second)
        if (firstRun)
        {
            Timber.d("First run")
            val currentNightMode = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            sharedPreferences.edit()
                .putBoolean(FIRST_RUN_SH.first, false)
                .putBoolean(DARK_MODE_SH.first, currentNightMode)
                .putLong(LAST_RUN_SH.first, System.currentTimeMillis())
                .apply()
        }
        else
        {
            Timber.d("Not first run")
        }
    }

    private fun delayedInit()
    {
        applicationScope.launch {
            createNotificationChannel()
            setupRecurringWork()
        }
    }

    // region WorkManager

    override fun getWorkManagerConfiguration(): androidx.work.Configuration =
        androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun setupRecurringWork()
    {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
//            .setRequiresCharging(true) // do not require charging
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            LOAD_DEFAULT_TIME,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }


    // endregion

    private fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                CHANNEL_FRESH_NEWS_ID,
                applicationContext.getString(R.string.channel_base_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = applicationContext.getString(R.string.channel_base_desc)
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

}