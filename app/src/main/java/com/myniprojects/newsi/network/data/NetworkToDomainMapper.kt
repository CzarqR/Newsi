package com.myniprojects.newsi.network.data

import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.utils.ModelMapper
import javax.inject.Inject

class NetworkToDomainMapper @Inject constructor() : ModelMapper<ApiNews, News>
{
    override fun mapToNewModel(entity: ApiNews): News
    {
        return News(
            entity.date,
            entity.description,
            entity.imageUrl,
            entity.title,
            entity.url
        )
    }
}