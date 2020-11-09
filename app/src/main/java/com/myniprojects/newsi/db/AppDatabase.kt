package com.myniprojects.newsi.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NewsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract val newsDao: NewsDao
}