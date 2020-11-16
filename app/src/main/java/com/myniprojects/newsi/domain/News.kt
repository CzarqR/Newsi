package com.myniprojects.newsi.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "domain_news")
data class News(
    val date: String,
    val desc: String?,
    val imageUrl: String?,
    val title: String,
    @PrimaryKey val url: String,
    var isLiked: Boolean = false
)
