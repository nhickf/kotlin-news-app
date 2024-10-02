package com.grpcx.androidtask.data.source.network.response


import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String?,
    val name: String?
)