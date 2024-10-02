package com.grpcx.androidtask.di

import com.grpcx.androidtask.data.source.network.NewsService
import com.grpcx.androidtask.data.repository.MainRepositoryImpl
import com.grpcx.androidtask.data.source.local.dao.NewsDao
import com.grpcx.androidtask.domain.repository.MainRepository
import com.grpcx.androidtask.domain.usecase.CacheArticles
import com.grpcx.androidtask.domain.usecase.LatestArticles
import com.grpcx.androidtask.domain.usecase.UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesMainRepository(newsDao: NewsDao, newsService: NewsService): MainRepository {
        return MainRepositoryImpl(
            newsDao = newsDao,
            newsService = newsService
        )
    }

    @Provides
    fun providesUseCase(repository: MainRepository): UseCase {
        return UseCase(
            latestArticles = LatestArticles(repository),
            cacheArticles = CacheArticles(repository),
        )
    }

}