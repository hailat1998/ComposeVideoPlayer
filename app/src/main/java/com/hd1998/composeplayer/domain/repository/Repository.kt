package com.hd1998.composeplayer.domain.repository

import com.hd1998.composeplayer.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getVideos(): Flow<List<Video>>
    suspend fun insertVideoS(video: List<Video>)
    fun enQueueWork()
}