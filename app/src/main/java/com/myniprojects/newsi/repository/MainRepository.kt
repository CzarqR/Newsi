package com.myniprojects.newsi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.domain.News
import kotlinx.coroutines.flow.Flow
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
        val pagingSourceFactory = { database.newsDao.getNews() }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 1
            ),
            remoteMediator = newsRemoteMediator.apply { setSearchKey(searchKey) },
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    companion object
    {
        private const val NETWORK_PAGE_SIZE = 50
    }
}