package com.myniprojects.newsi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao
{
    @Query("SELECT * FROM news_entity ORDER BY date DESC")
    fun getNews(): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(newsList: List<NewsEntity>)
}