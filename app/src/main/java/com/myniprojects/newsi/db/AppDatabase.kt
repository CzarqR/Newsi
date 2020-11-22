package com.myniprojects.newsi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myniprojects.newsi.domain.News

@Database(
    entities = [
        NewsEntity::class,
        RemoteKeys::class,
        News::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract val cacheNewsDao: NewsDao
    abstract val remoteKeysDao: RemoteKeysDao
    abstract val domainNewsDao: DomainNewsDao
}