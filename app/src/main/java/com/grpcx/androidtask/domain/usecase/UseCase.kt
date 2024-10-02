package com.grpcx.androidtask.domain.usecase

data class UseCase(
    val latestArticles: LatestArticles,
    val cacheArticles: CacheArticles,
)
