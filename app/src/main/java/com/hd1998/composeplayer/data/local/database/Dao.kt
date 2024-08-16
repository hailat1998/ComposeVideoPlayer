package com.hd1998.composeplayer.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hd1998.composeplayer.domain.model.Video
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Query("SELECT * FROM VIDEO")
    fun getAllVideo(): Flow<List<Video>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVideo(video: List<Video>)

    @Query("SELECT * FROM VIDEO")
    fun getAllVideoSnapShot():List<Video>
}