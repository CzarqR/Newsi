package com.myniprojects.newsi.network.data


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResult(
    val data: ApiData,
    val message: Any?,
    val status: String
)