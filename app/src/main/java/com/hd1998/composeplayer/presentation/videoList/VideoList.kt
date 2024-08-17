package com.hd1998.composeplayer.presentation.videoList

import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.hd1998.composeplayer.R
import com.hd1998.composeplayer.domain.model.Video


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoList(list: State<List<Video>>, querying: State<Boolean>,
              toPlayer:(url:String) -> Unit){
    var showMenu = remember { mutableStateOf(false)    }
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
             itemsIndexed(list.value, {index: Int, item: Video -> item.uri.path!! }){ _, item ->
                 VideoRow(video = item, toPlayer = toPlayer)
             }
            }
           }
        }
    }
    if(showMenu.value){
        CascadingMenu(showMenu = showMenu, list = list.value)
    }
}



@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun VideoRow(video: Video, toPlayer: (url: String) -> Unit) {
    val context = LocalContext.current

    val thumbnail: Bitmap =
       context.contentResolver.loadThumbnail(
            video.uri, Size(640, 480), null)
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
            Image(painter = BitmapPainter(thumbnail.asImageBitmap()), null,
                modifier = Modifier.size(60.dp,60.dp))
            Text(
                text = video.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
                    .alpha(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = video.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .alpha(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CascadingMenu(showMenu: MutableState<Boolean>, list: List<Video>) {

    var subMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.TopEnd) {
               DropdownMenu(
            expanded = showMenu.value,
            onDismissRequest = { showMenu.value = false }
        ) {
            DropdownMenuItem(
                text = { Row{
                    BasicText("Sort By")
                    Icon(Icons.Default.KeyboardArrowRight, null)
                }
                       },
                onClick = { subMenuExpanded = true }
            )

            if (subMenuExpanded) {
                DropdownMenu(
                    expanded = subMenuExpanded,
                    onDismissRequest = { subMenuExpanded = false },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    DropdownMenuItem(
                        text = { BasicText("Size") },
                        onClick = { list.sortByType("size") }
                    )
                    DropdownMenuItem(
                        text = { BasicText("Name") },
                        onClick = { list.sortByType("name")  }
                    )
                    DropdownMenuItem(
                        text = { BasicText("Date") },
                        onClick = { list.sortByType("date") }
                    )
                }
            }
        }
    }
}


fun List<Video>.sortByType(type: String) {
    when (type) {
        "size" -> this.sortedBy { it.size }
        "date" -> this.sortedBy { it.dateModified }
        else -> this.sortedBy { it.name }
    }
}