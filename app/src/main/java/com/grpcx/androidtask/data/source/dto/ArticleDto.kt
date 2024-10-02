package com.grpcx.androidtask.data.source.dto

import com.grpcx.androidtask.data.source.local.entities.Article as ArticleEntity
import com.grpcx.androidtask.data.source.local.entities.Source as SourceEntity
import com.grpcx.androidtask.data.source.network.response.Article as ArticleResponse
import com.grpcx.androidtask.domain.model.Article
import com.grpcx.androidtask.domain.model.Source

data class ArticleDto(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: SourceDto?,
    val title: String,
    val url: String,
    val urlToImage: String?
)

fun ArticleDto.toEntity() = ArticleEntity(
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = SourceEntity(source?.id.orEmpty(),source?.name.orEmpty()),
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun ArticleDto.toModel() = Article(
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = Source(source?.id.orEmpty(),source?.name.orEmpty()),
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun ArticleResponse.toDto() = ArticleDto (
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = source?.toSourceDto(),
    title = title,
    url = url,
    urlToImage = urlToImage
)

