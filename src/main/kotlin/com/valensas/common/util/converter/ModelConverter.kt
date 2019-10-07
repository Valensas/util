package com.valensas.common.util.converter

interface ModelConverter<E, M> {
    fun toEntity(model: M): E
    fun toModel(entity: E): M
}