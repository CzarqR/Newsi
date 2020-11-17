package com.myniprojects.newsi.ui

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import com.myniprojects.newsi.BuildConfig
import com.myniprojects.newsi.utils.Constants.DARK_MODE_SH
import com.myniprojects.newsi.utils.Constants.FIRST_RUN_SH
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application()
{

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate()
    {
        super.onCreate()
        if (BuildConfig.DEBUG)
        {
            Timber.plant(Timber.DebugTree())
        }

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
}