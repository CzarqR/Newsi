package com.myniprojects.newsi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao
{
    @Query("SELECT * FROM news_entity")
    fun getNews(): LiveData<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(newsList: List<NewsEntity>)
}