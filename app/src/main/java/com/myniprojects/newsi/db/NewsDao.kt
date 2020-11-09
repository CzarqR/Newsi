package com.myniprojects.newsi.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao
{
    @Query("SELECT * FROM news_entity ORDER BY date DESC LIMIT :limit")
    fun getNews(limit: Int): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(newsList: List<NewsEntity>)
}