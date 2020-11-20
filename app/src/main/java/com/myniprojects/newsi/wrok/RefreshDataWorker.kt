package com.myniprojects.newsi.wrok

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.myniprojects.newsi.repository.NewsRepository
import retrofit2.HttpException

class RefreshDataWorker @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val newsRepository: NewsRepository
) : CoroutineWorker(appContext, workerParams)
{
    override suspend fun doWork(): Result
    {
        return try
        {
            newsRepository.getSearchResultStream(null) //load trending news
            Result.success()
        }
        catch (e: HttpException)
        {
            Result.retry()
        }
    }
}