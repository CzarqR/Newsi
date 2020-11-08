package com.myniprojects.newsi.repository

import com.myniprojects.newsi.database.EntityToDomainMapper
import com.myniprojects.newsi.database.NewsDao
import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.network.NetworkToEntityMapper
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.utils.Constants.DEFAULT_LOADING_NUMBER
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    private var currentPage = 0
    private var limit = 0

    suspend fun getTrendingNewsNetwork(): Flow<DataState<List<News>>> =
        flow {
            emit(DataState.Loading)
            try
            {
                Timber.d("Loading at page $currentPage")
                val newsApi = newsRetrofit.getTrending(
                    DEFAULT_LOADING_NUMBER,
                    currentPage * DEFAULT_LOADING_NUMBER
                )
                val news = networkToEntityMapper.mapToNewModelList(newsApi.data.results)
                newsDao.insertAll(news)

                limit += news.size
                val newsEntityList = newsDao.getNews(limit)

                Timber.d("New limit ${(currentPage + 1) * DEFAULT_LOADING_NUMBER}")
                emit(DataState.Success(entityToDomainMapper.mapToNewModelList(newsEntityList)))
                currentPage++
            }
            catch (e: Exception)
            {
                e.message.logD()
                limit += DEFAULT_LOADING_NUMBER
                val newsEntityList = newsDao.getNews(limit)
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
