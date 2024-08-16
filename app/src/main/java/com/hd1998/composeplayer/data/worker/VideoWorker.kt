package com.hd1998.composeplayer.data.worker

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hd1998.composeplayer.data.local.database.VideoDatabase
import com.hd1998.composeplayer.domain.model.Video
import java.util.Date

class VideoWorker(private val database: VideoDatabase, context: Context,
                  workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        fillDb()
        return Result.success()
    }

   private suspend fun fillDb() {
        val videoList = mutableListOf<Video>()
        val resolver = applicationContext.contentResolver

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.DATE_ADDED
        )

        val query = resolver.query(
            collection,
            projection,
            null,
            null,
            null
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val modifiedDateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
            val addedDateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val modifiedDate = cursor.getLong(modifiedDateColumn)
                val addedDate = cursor.getLong(addedDateColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                        )
                videoList += Video(uri = contentUri, name = name, duration= duration, size = size,
                    dateModified =  Date(modifiedDate  * 1000), dataAdded = Date(addedDate  * 1000))
            }
        }
       Log.i("WORKER", "${videoList.size}")
       videoList.forEach{
           println(it.dateModified)
       }
       database.videoDao().insertAllVideo(videoList)
    }
}