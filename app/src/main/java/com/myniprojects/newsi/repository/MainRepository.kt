package com.myniprojects.newsi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myniprojects.newsi.db.AppDatabase
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.network.data.NetworkToDomainMapper
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


    fun getSearchResultStream(): Flow<PagingData<News>>
    {
        val pagingSourceFactory = { database.newsDao.getNews() }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false, initialLoadSize = 1),
            remoteMediator = newsRemoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    companion object
    {
        private const val NETWORK_PAGE_SIZE = 50
    }
}


//@Singleton
//class MainRepository @Inject constructor(
//    private val newsDao: NewsDao,
//    private val newsRetrofit: NewsRetrofit,
//    private val networkToEntityMapper: NetworkToEntityMapper,
//    private val entityToDomainMapper: EntityToDomainMapper
//)
//{
//    private var currentPage = 0
//    private var limit = 0
//
//    suspend fun getTrendingNewsNetwork(): Flow<DataState<List<News>>> =
//        flow {
//            emit(DataState.Loading)
//            try
//            {
//                Timber.d("Loading at page $currentPage")
//                val newsApi = newsRetrofit.getTrending(
//                    DEFAULT_LOADING_NUMBER,
//                    currentPage * DEFAULT_LOADING_NUMBER
//                )
//                val news = networkToEntityMapper.mapToNewModelList(newsApi.data.results)
//                newsDao.insertAll(news)
//
//                limit += news.size
//                val newsEntityList = newsDao.getNews(limit)
//
//                Timber.d("New limit ${(currentPage + 1) * DEFAULT_LOADING_NUMBER}")
//                emit(DataState.Success(entityToDomainMapper.mapToNewModelList(newsEntityList)))
//                currentPage++
//            }
//            catch (e: Exception)
//            {
//                e.message.logD()
//                limit += DEFAULT_LOADING_NUMBER
//                val newsEntityList = newsDao.getNews(limit)
//                emit(DataState.Error(entityToDomainMapper.mapToNewModelList(newsEntityList), e))
//            }
//        }


//    fun getSearchedNews(
//        textSearch: String,
//        number: Int,
//        offset: Int
//    ): Flow<DataState<List<News>>> =
//        flow {
//            emit(DataState.Loading)
//            try
//            {
//                val newsApi = newsRetrofit.getSearched(number, offset, textSearch)
//                val news = networkMapper.mapFromEntityList(newsApi.data.results)
//                emit(DataState.Success(news))
//            }
//            catch (e: Exception)
//            {
//                e.message.logD()
//                emit(DataState.Error(e))
//            }
//        }

//}
