package com.grpcx.androidtask.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grpcx.androidtask.data.source.local.entities.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(articles: List<Article>)

    @Query("SELECT * FROM article")
    suspend fun getArticles(): List<Article>

}