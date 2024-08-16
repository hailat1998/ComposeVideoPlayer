package com.hd1998.composeplayer.domain.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity
data class Video(@PrimaryKey val uri: Uri, val name: String,
                 val dateModified: Date, val dataAdded: Date,
                 val duration: Int,  val size: Int)
