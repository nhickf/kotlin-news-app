package com.grpcx.androidtask.data.source.network.response


import kotlinx.serialization.Serializable

@Serializable
data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)