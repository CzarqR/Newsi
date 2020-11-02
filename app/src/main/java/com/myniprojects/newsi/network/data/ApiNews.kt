package com.myniprojects.newsi.network.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiNews(
    val date: String,
    val description: String,
    val id: String,
    @Json(name = "image") val imageUrl: String?,
    val title: String,
    val url: String
)