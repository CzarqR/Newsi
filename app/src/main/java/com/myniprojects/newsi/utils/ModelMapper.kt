package com.myniprojects.newsi.utils

interface ModelMapper<OldModel, NewModel>
{
    fun mapToNewModel(entity: OldModel): NewModel

    fun mapToNewModelList(entities: List<OldModel>): List<NewModel>
    {
        return entities.map {
            mapToNewModel(it)
        }
    }
}