package com.myniprojects.newsi.di

import com.myniprojects.newsi.network.NewsRetrofit
import com.myniprojects.newsi.utils.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule
{
    @Singleton
    @Provides
    fun provideRetrofit(scalar: ScalarsConverterFactory): Retrofit = Retrofit.Builder()
        .addConverterFactory(scalar)
        .baseUrl(Constants.BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideScalar(): ScalarsConverterFactory =
        ScalarsConverterFactory.create()

//    @Singleton
//    @Provides
//    fun provideMoshi(): ScalarsConverterFactory =
//        Moshi.create()

    @Singleton
    @Provides
    fun provideBlogService(retrofit: Retrofit): NewsRetrofit =
        retrofit.create(NewsRetrofit::class.java)
}