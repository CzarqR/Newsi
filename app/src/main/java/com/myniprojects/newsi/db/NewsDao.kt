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

    @Query("DELETE FROM news_entity WHERE isLiked = 'false'")
    suspend fun clearNewsNotLiked()

    @Query("UPDATE news_entity SET isLiked = :isLiked WHERE url = :url")
    suspend fun changeLike(isLiked: Boolean, url: String)
}

// It should have been in one entity but I don't know how to connect cache and saving in one table \\todo maybe in future

@Dao
interface DomainNewsDao
{
    @Query("SELECT * FROM domain_news ORDER BY date DESC")
    fun getNewsPaged(): PagingSource<Int, News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(likeNewsEntity: News)

    @Query("DELETE FROM domain_news WHERE url=:url")
    suspend fun delete(url: String)

    @Query("SELECT COUNT(url) FROM domain_news WHERE url=:url")
    suspend fun checkIfLiked(url: String): Int
}