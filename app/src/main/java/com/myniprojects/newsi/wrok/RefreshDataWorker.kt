package com.myniprojects.newsi.wrok

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.myniprojects.newsi.repository.NewsRepository
import com.myniprojects.newsi.utils.Constants
import com.myniprojects.newsi.utils.Constants.LAST_RUN_SH
import com.myniprojects.newsi.utils.Constants.LOAD_AFTER_MIN
import com.myniprojects.newsi.utils.createFreshNewsNotification
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val newsRepository: NewsRepository,
    private val sharedPreferences: SharedPreferences,
) : CoroutineWorker(appContext, workerParams)
{
    override suspend fun doWork(): Result
    {
        return try
        {
            Timber.d("Do work!")
            val lastRun = sharedPreferences.getLong(LAST_RUN_SH.first, LAST_RUN_SH.second)
            Timber.d("lastRun = $lastRun")
            val current = System.currentTimeMillis()
            Timber.d("current = $current")

            if (current > lastRun + LOAD_AFTER_MIN)
            {
                Timber.d("DO WORK ALL GOOD")

                loadDataAndShowNot()

                Timber.d("End Do work!")
            }

            Result.success()
        }
        catch (e: HttpException)
        {
            Result.retry()
        }
    }


    private fun loadDataAndShowNot()
    {
        newsRepository.getSearchResultStream(null) //load trending news

        val notificationManager = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            applicationContext.getSystemService(NotificationManager::class.java)
        }
        else
        {
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        notificationManager?.notify(
            Constants.FRESH_NEWS_NOTIFICATION_ID,
            applicationContext.createFreshNewsNotification()
        )
    }
}