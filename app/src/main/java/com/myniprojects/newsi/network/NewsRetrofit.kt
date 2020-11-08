package com.myniprojects.newsi.network

import com.myniprojects.newsi.network.data.ApiResult
import com.myniprojects.newsi.utils.Constants.HEADER_HOST
import com.myniprojects.newsi.utils.Constants.HEADER_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface NewsRetrofit
{
    @Headers(
        HEADER_HOST,
        HEADER_KEY
    )
    @GET("trending?language=en")
    suspend fun getTrending(
        @Query("number") number: Int,
        @Query("offset") page: Int,
    ): ApiResult

    @Headers(
        HEADER_HOST,
        HEADER_KEY
    )
    @GET("search?language=en")
    suspend fun getSearched(
        @Query("number") number: Int,
        @Query("offset") offset: Int,
        @Query("q") searchText: String
    ): ApiResult


}