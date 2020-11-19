package com.myniprojects.newsi.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myniprojects.newsi.domain.News

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

    @Query("SELECT COUNT(url) FROM domain_news")
    fun countLiked(): LiveData<Long>
}