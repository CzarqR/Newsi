package com.myniprojects.newsi.network

import com.myniprojects.newsi.model.News
import com.myniprojects.newsi.network.data.ApiNews
import com.myniprojects.newsi.utils.EntityMapper
import javax.inject.Inject

class NetworkMapper @Inject constructor() : EntityMapper<ApiNews, News>
{
    override fun mapFromEntity(entity: ApiNews): News
    {
        return News(
            entity.date,
            entity.description,
            entity.id,
            entity.imageUrl,
            entity.title,
            entity.url
        )
    }

    override fun mapToEntity(domainModel: News): ApiNews
    {
        TODO("Not yet implemented")
    }
}