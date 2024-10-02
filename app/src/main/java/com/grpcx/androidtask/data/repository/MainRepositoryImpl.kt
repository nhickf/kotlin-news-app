package com.grpcx.androidtask.data.repository

import com.grpcx.androidtask.data.source.dto.toDto
import com.grpcx.androidtask.data.source.dto.toModel
import com.grpcx.androidtask.data.source.local.dao.NewsDao
import com.grpcx.androidtask.data.source.local.entities.toModel
import com.grpcx.androidtask.data.source.network.NewsService
import com.grpcx.androidtask.domain.model.Article
import com.grpcx.androidtask.domain.repository.MainRepository
import com.grpcx.androidtask.util.NetworkResponse
import com.grpcx.androidtask.util.RepositoryResponse
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsService: NewsService
) : MainRepository {

    override suspend fun fetchLatestArticles(): RepositoryResponse<List<Article>> {
        return when (val result = newsService.fetchEverything()) {
            is NetworkResponse.Error ->  RepositoryResponse.Error(result.code, result.message)
            is NetworkResponse.Exception -> RepositoryResponse.Exception(result.e)
            is NetworkResponse.Success -> RepositoryResponse.Success( result.data.articles.map { it.toDto().toModel() })
        }
    }

    override suspend fun fetchCacheArticles(): RepositoryResponse<List<Article>> {
        return try {
            RepositoryResponse.Success(newsDao.getArticles().map { it.toModel() })
        }catch (e: Exception){
            RepositoryResponse.Exception(e)
        }
    }

}