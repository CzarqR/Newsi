package com.myniprojects.newsi.ui

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.myniprojects.newsi.BuildConfig
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.FIRST_RUN_SH
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
                .apply()
        }
        else
        {
            Timber.d("Not first run")
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
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            REFRESH_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    private fun delayedInit()
    {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    // endregion

}