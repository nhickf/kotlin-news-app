package com.grpcx.androidtask.domain.usecase

import com.grpcx.androidtask.domain.repository.MainRepository

class LatestArticles (private val repository: MainRepository) {

    suspend fun invoke() = repository.fetchLatestArticles()

}