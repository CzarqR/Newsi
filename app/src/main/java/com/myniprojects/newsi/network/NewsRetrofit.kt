package com.myniprojects.newsi.network

import com.myniprojects.newsi.network.data.ApiData
import com.myniprojects.newsi.network.data.ApiResult
import com.myniprojects.newsi.utils.Constants.HEADER_HOST
import com.myniprojects.newsi.utils.Constants.HEADER_KEY
import retrofit2.Call
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
    suspend fun getTrending(@Query("number") number: Int): ApiResult
}