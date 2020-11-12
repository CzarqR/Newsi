package com.myniprojects.newsi.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.db.RemoteKeys
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NetworkToEntityMapper
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.utils.Constants.NEWS_STARTING_PAGE_INDEX
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val newsRetrofit: NewsRetrofit,
    private val database: AppDatabase,
    private val networkToEntityMapper: NetworkToEntityMapper
) : RemoteMediator<Int, News>()
{
    override suspend fun load(loadType: LoadType, state: PagingState<Int, News>): MediatorResult
    {
        val page: Int = when (loadType)
        {
            LoadType.REFRESH ->
            {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: NEWS_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND ->
            {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                remoteKeys.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
            LoadType.APPEND ->
            {
                Timber.d("Append")
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null)
                {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        try
        {
            val apiResponse = newsRetrofit.getTrending(
                state.config.pageSize,
                page * state.config.pageSize
            )

            val news = apiResponse.data.results
            val endOfPaginationReached = news.isEmpty()

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH)
                {
                    database.remoteKeysDao.clearRemoteKeys()
                    database.newsDao.clearNews()
                }

                val prevKey = if (page == NEWS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = news.map {
                    RemoteKeys(
                        newsId = it.url,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                database.remoteKeysDao.insertAll(keys)
                database.newsDao.insertAll(networkToEntityMapper.mapToNewModelList(news))
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }
        catch (exception: IOException)
        {
            return MediatorResult.Error(exception)
        }
        catch (exception: HttpException)
        {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, News>): RemoteKeys?
    {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { news ->
                database.remoteKeysDao.remoteKeysRepoId(news.url)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, News>): RemoteKeys?
    {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { news ->
                database.remoteKeysDao.remoteKeysRepoId(news.url)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, News>
    ): RemoteKeys?
    {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { newsUrl ->
                database.remoteKeysDao.remoteKeysRepoId(newsUrl)
            }
        }
    }
}