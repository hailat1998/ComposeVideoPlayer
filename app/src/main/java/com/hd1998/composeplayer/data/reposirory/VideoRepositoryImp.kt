package com.hd1998.composeplayer.data.reposirory

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.hd1998.composeplayer.data.local.database.VideoDatabase
import com.hd1998.composeplayer.data.worker.VideoWorker
import com.hd1998.composeplayer.domain.model.Video
import com.hd1998.composeplayer.domain.repository.Repository
import com.hd1998.composeplayer.presentation.videoList.sortByType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


const val WORK_NAME = "video"
class VideoRepositoryImp(private val workManager: WorkManager,
                         private val database: VideoDatabase): Repository {

                             init{
                                 enQueueWork()
                             }

    override fun getVideos(): Flow<List<Video>> {
          val list = database.videoDao().getAllVideoSnapShot()
          println(list.size)
          return database.videoDao().getAllVideo()
    }

    override suspend fun insertVideoS(video: List<Video>) {
        TODO("Not yet implemented")
    }

    override fun enQueueWork(){
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<VideoWorker>().build()
        workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, oneTimeWorkRequest)
    }
                         }