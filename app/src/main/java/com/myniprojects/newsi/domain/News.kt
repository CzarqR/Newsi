package com.myniprojects.newsi.domain

data class News(
    val date: String,
    val desc: String?,
    val imageUrl: String?,
    val title: String,
    val url: String,
    var isLiked: Boolean
)
