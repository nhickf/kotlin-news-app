package com.grpcx.androidtask.data.source.network

import com.grpcx.androidtask.data.source.network.response.News
import com.grpcx.androidtask.util.NetworkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("/v2/top-headlines")
    suspend fun fetchHeadlines(
        @Query("q") query: String = "Philippines"
    ): NetworkResponse<News>

    @GET("/v2/everything")
    suspend fun fetchEverything(
        @Query("q") query: String = "Philippines"
    ): NetworkResponse<News>

}