package com.myniprojects.newsi.network.data


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiData(
    val language: String,
    val number: String,
    val offset: Int,
    val results: List<ApiNews>,
    @Json(name = "results_count") val resultsCount: Int?
)