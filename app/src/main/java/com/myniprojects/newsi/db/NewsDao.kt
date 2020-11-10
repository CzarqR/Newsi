package com.myniprojects.newsi.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myniprojects.newsi.domain.News

@Dao
interface NewsDao
{
    @Query("SELECT * FROM news_entity ORDER BY date DESC")
    fun getNews(): PagingSource<Int, News>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(newsList: List<NewsEntity>)

    @Query("DELETE FROM news_entity")
    suspend fun clearNews()
}