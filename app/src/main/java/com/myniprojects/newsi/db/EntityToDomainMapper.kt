package com.myniprojects.newsi.db

import com.myniprojects.newsi.domain.News
import com.myniprojects.newsi.utils.ModelMapper
import javax.inject.Inject

class EntityToDomainMapper @Inject constructor() : ModelMapper<NewsEntity, News>
{
    override fun mapToNewModel(entity: NewsEntity): News
    {
        return News(
            entity.date,
            entity.desc,
            entity.imageUrl,
            entity.title,
            entity.url,
            entity.isLiked
        )
    }

}