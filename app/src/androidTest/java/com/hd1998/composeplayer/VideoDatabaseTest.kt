package com.hd1998.composeplayer

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hd1998.composeplayer.data.local.database.Dao
import com.hd1998.composeplayer.data.local.database.VideoDatabase
import com.hd1998.composeplayer.domain.model.Video
import androidx.room.Room
import org.junit.runner.RunWith
import android.content.Context
import android.net.Uri
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import java.util.Date
import org.junit.After
import org.junit.Before
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class VideoDatabaseTest {
    private lateinit var db: VideoDatabase
    private lateinit var Dao: Dao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, VideoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        Dao = db.videoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }



    @Test
    fun testQueryVideo() = runBlocking {
        Dao.insertAllVideo(generateRandomVideoData())

        val videos = Dao.getAllVideoSnapShot()
        assertEquals(videos.size, 3)
        assertEquals(videos[0].uri, generateRandomVideoData()[0].uri )
        assert(generateRandomVideoData().contains(videos[1]))
    }
    fun generateRandomVideoData(): List<Video> {
        return listOf(
            Video(
                uri = Uri.parse("content://media/external/video/media/1"),
                name = "Sample Video 1",
                dateModified = Date(1622505600000),
                dateAdded = Date(1622592000000),
                duration = 600000, // 10 minutes in milliseconds
                size = 10485760, // 10 MB

            ),
            Video(
                uri = Uri.parse("content://media/external/video/media/2"),
                name = "Sample Video 2",
                dateModified = Date(1625184000000),
                dateAdded = Date(1625270400000),
                duration = 300000, // 5 minutes in milliseconds
                size = 5242880 ,// 5 MB
            ),
            Video(
                uri = Uri.parse("content://media/external/video/media/3"),
                name = "Sample Video 3",
                dateModified = Date(1627776000000),
                dateAdded = Date(1627862400000),
                duration = 900000, // 15 minutes in milliseconds
                size = 15728640, // 15 MB
            )
        )
    }

}