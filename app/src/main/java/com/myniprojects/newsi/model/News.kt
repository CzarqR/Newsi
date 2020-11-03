package com.myniprojects.newsi.model

import android.graphics.Bitmap

data class News(
    val date: String,
    val desc: String,
    val id: String,
    val imageUrl: String?,
    val title: String,
    val url: String
)
{
    val image: Bitmap? = null
    var isLiked: Boolean = false
}
