package com.hd1998.composeplayer.presentation.player

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView


@OptIn(UnstableApi::class)
@Composable
fun Player(uri: String){


    val context = LocalContext.current

    val dataSourceFactory: DataSource.Factory = remember {
        DefaultDataSource.Factory(
            context
        )
    }
    val mediaSource: MediaSource = remember {
        ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
    }


    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    exoPlayer.setMediaSource(mediaSource)
    exoPlayer.prepare()
    exoPlayer.playWhenReady = true

    AndroidView(factory = {
        PlayerView(it).apply {
            useController = true
            player = exoPlayer
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
          }
       }
    )
}