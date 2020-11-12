package com.myniprojects.newsi.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NewsEntity::class,
        RemoteKeys::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract val newsDao: NewsDao
    abstract val remoteKeysDao: RemoteKeysDao
}