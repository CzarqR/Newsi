package com.myniprojects.newsi.di

import android.content.Context
import android.content.SharedPreferences
import com.myniprojects.newsi.utils.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ibrahimsn.library.LiveSharedPreferences
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object SharedPreferencesModule
{
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideLiveSharedPreferences(sharedPreferences: SharedPreferences) =
        LiveSharedPreferences(sharedPreferences)
}