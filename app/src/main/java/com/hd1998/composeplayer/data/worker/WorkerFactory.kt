package com.hd1998.composeplayer.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.hd1998.composeplayer.data.local.database.VideoDatabase

class MyWorkerFactory(
    private val database: VideoDatabase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            VideoWorker::class.java.name -> {
                VideoWorker(database, appContext, workerParameters)
            }
            else -> null
        }
    }
}
