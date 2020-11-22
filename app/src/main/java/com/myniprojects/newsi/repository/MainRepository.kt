package com.myniprojects.newsi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.utils.Constants.INITIAL_LOAD_SIZE
import com.myniprojects.newsi.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NewsRepository @Inject constructor(
    private val database: AppDatabase,
    private val newsRemoteMediator: NewsRemoteMediator
)
{
    // PagingSource, old code, single source without cache
//    fun getSearchResultStream(): Flow<PagingData<News>>
//    {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { NewsPagingSource(newsRetrofit, networkToDomainMapper) }
//        ).flow
//    }


    fun getSearchResultStream(searchKey: String?): Flow<PagingData<News>>
    {
        Timber.d("getSearchResultStream")
        val pagingSourceFactory = { database.cacheNewsDao.getNews() }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            remoteMediator = newsRemoteMediator.apply { this.searchKey = searchKey },
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getLikedNews(): Flow<PagingData<News>>
    {
        Timber.d("getLikedNews")

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = { database.domainNewsDao.getNewsPaged() }
        ).flow

    }

}