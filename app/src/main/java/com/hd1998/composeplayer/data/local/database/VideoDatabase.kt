package com.hd1998.composeplayer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hd1998.composeplayer.domain.model.Video


@Database(entities = [Video::class], version = 1)
@TypeConverters(value = [Converters::class])
abstract class VideoDatabase: RoomDatabase() {
    abstract fun videoDao() : Dao
}