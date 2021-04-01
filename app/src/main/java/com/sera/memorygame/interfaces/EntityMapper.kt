package com.sera.memorygame.interfaces

interface EntityMapper<Entity,Model> {

    fun mapFromEntity(entity: Entity): Model

    fun mapToEntity(domainModel: Model): Entity

}