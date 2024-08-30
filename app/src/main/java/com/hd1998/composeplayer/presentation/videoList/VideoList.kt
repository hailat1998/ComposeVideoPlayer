package com.hd1998.composeplayer.presentation.videoList

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.hd1998.composeplayer.R
import com.hd1998.composeplayer.domain.model.Video
import kotlinx.coroutines.selects.select
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoList(list: State<List<Video>>, querying: State<Boolean>,
              toPlayer:(url:String) -> Unit){
    val showMenu = remember { mutableStateOf(false)    }
    val mutableList = list.value.toMutableStateList()
    var showInfo by remember { mutableStateOf(false)  }
    var selected by remember { mutableStateOf( Video(
        uri = Uri.parse("content://media/external/video/media/1"),
        name = "Sample Video 1",
        dateModified = Date(1622505600000),
        dateAdded = Date(1622592000000),
        duration = 600000,
        size = 10485760,

    ),
        ) }
    Scaffold(topBar = {TopAppBar(title = {
        Text(
            text = "Video",
            style = MaterialTheme.typography.displayMedium
        )
    },
    actions = {
        Icon(Icons.Default.MoreVert, null,
            modifier = Modifier.clickable { showMenu.value = true })
          }
        )
      }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
          Image(painterResource(id = R.drawable.background), null,
              modifier = Modifier.fillMaxSize(),
              colorFilter = ColorFilter.tint(Color.DarkGray, blendMode = BlendMode.Darken),
              contentScale = ContentScale.FillBounds
              )
            if(querying.value){
                Image( painter = rememberDrawablePainter(
                    drawable = getDrawable(
                        LocalContext.current,
                        R.drawable.loading_vi
                    )
                ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize())
            }else{
                LazyColumn {
             itemsIndexed(mutableList, {index: Int, item: Video -> item.uri.path!! }){ _, item ->
                 VideoRow(video = item, toPlayer = toPlayer){
                           selected = it
                     showInfo = true
                 }
             }
            }
           }
        }
        if(showMenu.value){
            CascadingMenu(showMenu = showMenu){ s ->
                Log.i("SORTING", "here")
                mutableList.sortByType(s)
            }
        }
        if(showInfo){
            VideoInfoDialog(video = selected) {
                showInfo = false
            }
        }
    }

}



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoRow(video: Video, toPlayer: (url: String) -> Unit,
             showInfo: (video: Video) -> Unit) {
    val context = LocalContext.current

    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormat.format(video.dateAdded)

    val name = if(video.name.length > 15) video.name.substring(0, 16) + "..." else video.name
    val thumbnail: Bitmap =
        context.contentResolver.loadThumbnail(
            video.uri, Size(640, 480), null
        )
    Card(
        modifier = Modifier
            .shadow(7.dp, RoundedCornerShape(7.dp))
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
            .alpha(0.8f) // Set alpha for the Card
            .clickable { toPlayer.invoke(video.uri.toString()) }
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
            // .alpha(0.5f) // Set alpha for the Row
        ) {
            Image(
                painter = BitmapPainter(thumbnail.asImageBitmap()), null,
                modifier = Modifier.size(60.dp, 60.dp).padding(end = 20.dp)
            )
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .alpha(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .alpha(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Icon(Icons.Default.Info, null,
               Modifier.clickable {
                   showInfo.invoke(video)
               }.offset(20.dp, 5.dp)
                   .size(40.dp)
            )
        }
    }
}

@Composable
fun CascadingMenu(showMenu: MutableState<Boolean>, sortType:(s:String) -> Unit) {

    var subMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.TopEnd) {
        if (!subMenuExpanded) {
            DropdownMenu(
                expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false },
                offset = DpOffset(260.dp, 20.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Row {
                            BasicText("Sort By")
                            Icon(Icons.Default.KeyboardArrowRight, null)
                        }
                    },
                    onClick = { subMenuExpanded = true }
                )
            }
        }else {
                DropdownMenu(
                    expanded = subMenuExpanded ,
                    onDismissRequest = { subMenuExpanded = false
                                        showMenu.value = false },
                    modifier = Modifier.padding(start = 16.dp),
                    offset = DpOffset(260.dp, 20.dp)
                ) {
                    DropdownMenuItem(
                        text = { BasicText("Size") },
                        onClick = { sortType.invoke("size") }
                    )
                    DropdownMenuItem(
                        text = { BasicText("Name") },
                        onClick = { sortType.invoke("name")  }
                    )
                    DropdownMenuItem(
                        text = { BasicText("Date") },
                        onClick = { sortType.invoke("date") }
                    )
                }
            }
        }
    }




fun MutableList<Video>.sortByType(type: String) {
    when (type) {
        "size" -> this.sortBy { it.size }
        "date" -> this.sortBy { it.dateModified }
        else -> this.sortBy { it.name }
    }
}

@Composable
fun VideoInfoDialog(video: Video, onDismiss: () -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Video Info") },
        text = {
            Column {
                val size = formatSize(video.size.toLong())
                val duration = formatDuration(video.duration)
                Text(text = "Name: ${video.name}")
                Text(text = "Date Modified: ${dateFormat.format(video.dateModified)}")
                Text(text = "Date Added: ${dateFormat.format(video.dateAdded)}")
                Text(text = "Duration: $duration")
                Text(text = "Size: $size")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

fun formatSize(sizeInBytes: Long): String {
    val kiloByte = 1024.0
    val megaByte = kiloByte * 1024
    val gigaByte = megaByte * 1024
    val teraByte = gigaByte * 1024

    return when {
        sizeInBytes >= teraByte -> String.format("%.2f TB", sizeInBytes / teraByte)
        sizeInBytes >= gigaByte -> String.format("%.2f GB", sizeInBytes / gigaByte)
        sizeInBytes >= megaByte -> String.format("%.2f MB", sizeInBytes / megaByte)
        else -> String.format("%.2f KB", sizeInBytes / kiloByte)
    }
}

fun formatDuration(seconds: Int): String {
    val seconds = seconds / 1000
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}