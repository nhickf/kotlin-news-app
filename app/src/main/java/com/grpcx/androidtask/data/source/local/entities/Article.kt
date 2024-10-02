package com.grpcx.androidtask.data.source.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.grpcx.androidtask.domain.model.Source as SourceModel
import com.grpcx.androidtask.domain.model.Article as ArticleModel

@Entity
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    @Embedded
    val source: Source?,
    val title: String,
    @PrimaryKey
    val url: String,
    val urlToImage: String?
)

fun Article.toModel() = ArticleModel(
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = SourceModel(source?.sourceId.orEmpty(),source?.sourceName.orEmpty()),
    title = title,
    url = url,
    urlToImage = urlToImage
)