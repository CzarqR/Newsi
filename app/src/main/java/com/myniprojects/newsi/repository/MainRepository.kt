package com.myniprojects.newsi.repository

import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.network.data.ApiResult
import com.myniprojects.newsi.utils.DataState
import com.myniprojects.newsi.utils.logD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val newsRetrofit: NewsRetrofit
)
{
    suspend fun getTrendingNews(): Flow<DataState<ApiResult>> =
        flow {
            emit(DataState.Loading)
            try
            {
                val news = newsRetrofit.getTrending(5)
                emit(DataState.Success(news))
            }
            catch (e: Exception)
            {
                e.message.logD()
                emit(DataState.Error(e))
            }
        }
}