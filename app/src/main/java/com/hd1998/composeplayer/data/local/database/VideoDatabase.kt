package com.hd1998.composeplayer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hd1998.composeplayer.domain.model.Video


@Database(entities = [Video::class], version = 3)
@TypeConverters(value = [Converters::class])
abstract class VideoDatabase: RoomDatabase() {
    abstract fun videoDao() : Dao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Video_new (
                uri TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                dateModified INTEGER NOT NULL,
                dateAdded INTEGER NOT NULL,
                duration INTEGER NOT NULL,
                size INTEGER NOT NULL,
                artist TEXT NOT NULL,
                album TEXT NOT NULL 
            )
        """.trimIndent())


        db.execSQL("""
            INSERT INTO Video_new (uri, name, dateModified, dateAdded, duration, size)
            SELECT uri, name, dateModified, dateAdded, duration, size FROM Video
        """.trimIndent())


        db.execSQL("DROP TABLE Video")


        db.execSQL("ALTER TABLE Video_new RENAME TO Video")
    }
}

val MIGRATION_2_3 = object : Migration(2,3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Video_new (
                uri TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                dateModified INTEGER NOT NULL,
                dateAdded INTEGER NOT NULL,
                duration INTEGER NOT NULL,
                size INTEGER NOT NULL
                  )
        """.trimIndent())


        db.execSQL("""
            INSERT INTO Video_new (uri, name, dateModified, dateAdded, duration, size)
            SELECT uri, name, dateModified, dateAdded, duration, size FROM Video
        """.trimIndent())


        db.execSQL("DROP TABLE Video")


        db.execSQL("ALTER TABLE Video_new RENAME TO Video")
    }
}