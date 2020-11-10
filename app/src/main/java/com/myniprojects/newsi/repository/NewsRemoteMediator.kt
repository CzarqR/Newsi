package com.myniprojects.newsi.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NewsRetrofit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val newsRetrofit: NewsRetrofit,
    private val database: AppDatabase
) : RemoteMediator<Int, News>()
{
    override suspend fun load(loadType: LoadType, state: PagingState<Int, News>): MediatorResult
    {
        TODO("Not yet implemented")
    }
}