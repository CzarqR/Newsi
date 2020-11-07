package com.myniprojects.newsi.repository

import com.myniprojects.newsi.database.EntityToDomainMapper
import com.myniprojects.newsi.database.NewsDao
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NetworkToEntityMapper
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val newsRetrofit: NewsRetrofit,
    private val networkToEntityMapper: NetworkToEntityMapper,
    private val entityToDomainMapper: EntityToDomainMapper
)
{

    suspend fun getTrendingNewsNetwork(
        number: Int,
        offset: Int
    ): Flow<DataState<List<News>>> =
        flow {
            emit(DataState.Loading)
            try
            {
                val newsApi = newsRetrofit.getTrending(number, offset)
                val news = networkToEntityMapper.mapToNewModelList(newsApi.data.results)
                newsDao.insertAll(news)

                val newsEntityList = newsDao.getNews()
                emit(DataState.Success(entityToDomainMapper.mapToNewModelList(newsEntityList)))
            }
            catch (e: Exception)
            {
                e.message.logD()

                val newsEntityList = newsDao.getNews()
                emit(DataState.Error(entityToDomainMapper.mapToNewModelList(newsEntityList), e))

            }
        }


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

}
