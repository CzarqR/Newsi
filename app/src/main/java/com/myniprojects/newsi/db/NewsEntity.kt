package com.myniprojects.newsi.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_entity")
data class NewsEntity(
    @PrimaryKey val url: String,
    val title: String,
    val desc: String?,
    val imageUrl: String?,
    val date: String,
    val isLiked: Boolean = false
)