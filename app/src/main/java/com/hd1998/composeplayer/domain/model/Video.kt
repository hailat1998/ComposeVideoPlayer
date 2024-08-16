package com.hd1998.composeplayer.domain.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Immutable
@Entity
data class Video(@PrimaryKey val uri: Uri, val name: String,
                 val dateModified: Date, val dateAdded: Date,
                 val duration: Int,  val size: Int,
    val artist: String, val album: String)
