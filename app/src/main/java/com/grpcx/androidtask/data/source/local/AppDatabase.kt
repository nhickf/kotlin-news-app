package com.grpcx.androidtask.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grpcx.androidtask.data.source.local.dao.NewsDao
import com.grpcx.androidtask.data.source.local.entities.Article

@Database(entities = [Article::class], version = 1)
@TypeConverters(SourceConverter::class)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun newsDao(): NewsDao
}