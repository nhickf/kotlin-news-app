package com.grpcx.androidtask.domain.usecase

import com.grpcx.androidtask.domain.repository.MainRepository


class CacheArticles (private val repository: MainRepository) {

    suspend fun invoke() = repository.fetchCacheArticles()

}