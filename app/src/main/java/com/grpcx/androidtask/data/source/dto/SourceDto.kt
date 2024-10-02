package com.grpcx.androidtask.data.source.dto

import com.grpcx.androidtask.domain.model.Source
import com.grpcx.androidtask.data.source.network.response.Source as SourceResponse
import com.grpcx.androidtask.data.source.local.entities.Source as SourceEntity


data class SourceDto(
    val id: String?,
    val name: String?
)


fun SourceResponse.toSourceDto() = SourceDto(
    id = id,
    name = name
)

fun SourceDto.toModel() = Source(
    id = id.orEmpty(),
    name = name.orEmpty()
)

fun SourceDto.toEntity() = SourceEntity(
    sourceId = id.orEmpty(),
    sourceName = name.orEmpty()
)