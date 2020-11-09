package com.myniprojects.newsi.network

import com.myniprojects.newsi.db.NewsEntity
import com.myniprojects.newsi.network.data.ApiNews
import com.myniprojects.newsi.utils.ModelMapper
import javax.inject.Inject

class NetworkToEntityMapper @Inject constructor() : ModelMapper<ApiNews, NewsEntity>
{
    override fun mapToNewModel(entity: ApiNews): NewsEntity
    {
        return NewsEntity(
            entity.url,
            entity.title,
            entity.description,
            entity.imageUrl,
            entity.date
        )
    }
}