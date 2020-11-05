package com.myniprojects.newsi.repository

import com.myniprojects.newsi.model.News
import com.myniprojects.newsi.network.NetworkMapper
import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val newsRetrofit: NewsRetrofit,
    private val networkMapper: NetworkMapper
)
{
    suspend fun getSearchedNews(
        textSearch: String,
        number: Int,
        offset: Int
    ): Flow<DataState<List<News>>> =
        flow {
            emit(DataState.Loading)
            try
            {
                val newsApi = newsRetrofit.getSearched(number, offset, textSearch)
                val news = networkMapper.mapFromEntityList(newsApi.data.results)
                emit(DataState.Success(news))
            }
            catch (e: Exception)
            {
                e.message.logD()
                emit(DataState.Error(e))
            }
        }

    suspend fun getTrendingNews(
        number: Int,
        offset: Int
    ): Flow<DataState<List<News>>> =
        flow {
            emit(DataState.Loading)
            try
            {
                val newsApi = newsRetrofit.getTrending(number, offset)
                val news = networkMapper.mapFromEntityList(newsApi.data.results)
                emit(DataState.Success(news))
            }
            catch (e: Exception)
            {
                e.message.logD()
                emit(DataState.Error(e))
            }
        }
}