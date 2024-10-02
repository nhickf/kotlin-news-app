package com.grpcx.androidtask.di

import com.grpcx.androidtask.data.source.network.NetworkResponseCallAdapterFactory
import com.grpcx.androidtask.data.source.network.NewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.internal.addHeaderLenient
import okhttp3.internal.http2.Header
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesOkhttpClient(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authenticator = Authenticator { _, response ->
            response
                .request
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer 19088d93d0c640c9aa38d92d9d3ad707"
                )
                .build()
        }

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    fun providesNewsService(okHttpClient: OkHttpClient): NewsService {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NewsService::class.java)
    }


}