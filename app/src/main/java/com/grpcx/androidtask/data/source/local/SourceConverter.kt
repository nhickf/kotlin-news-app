package com.grpcx.androidtask.data.source.local

import androidx.room.TypeConverter
import com.grpcx.androidtask.data.source.local.entities.Source

class SourceConverter {

    @TypeConverter
    fun fromSource(source: Source?): String {
        return source?.sourceName.orEmpty()
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}