package com.grpcx.androidtask.domain.repository

import com.grpcx.androidtask.domain.model.Article
import com.grpcx.androidtask.util.RepositoryResponse
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun fetchLatestArticles(): RepositoryResponse<List<Article>>

    suspend fun fetchCacheArticles(): RepositoryResponse<List<Article>>

}