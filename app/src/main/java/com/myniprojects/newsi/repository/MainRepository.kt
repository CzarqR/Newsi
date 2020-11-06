package com.myniprojects.newsi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.myniprojects.newsi.database.EntityToDomainMapper
import com.myniprojects.newsi.database.NewsDao
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NetworkToEntityMapper
import com.myniprojects.newsi.network.NewsRetrofit
import kotlinx.coroutines.Dispatchers
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

    val news: LiveData<List<News>> = Transformations.map(newsDao.getNews()) {
        entityToDomainMapper.mapToNewModelList(it)
    }


    suspend fun getNews(number: Int, offset: Int)
    {
        withContext(Dispatchers.IO) {
            Timber.d("Before search")
            val apiResult = newsRetrofit.getTrending(number, offset)
            Timber.d("After search")
            val news = networkToEntityMapper.mapToNewModelList(apiResult.data.results)
            newsDao.insertAll(news)
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

//    suspend fun getTrendingNews(
//        number: Int,
//        offset: Int
//    ): Flow<DataState<List<News>>> =
//        flow {
//            emit(DataState.Loading)
//            try
//            {
//                val newsApi = newsRetrofit.getTrending(number, offset)
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