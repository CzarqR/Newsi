package com.myniprojects.newsi.adapters

import com.myniprojects.newsi.domain.News

sealed class NewsRecyclerModel
{
    data class NewsItem(val news: News) : NewsRecyclerModel()
    data class SeparatorItem(val day: String) : NewsRecyclerModel()
}