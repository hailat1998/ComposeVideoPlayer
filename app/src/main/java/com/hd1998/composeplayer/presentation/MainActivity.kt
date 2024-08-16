package com.hd1998.composeplayer.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hd1998.composeplayer.presentation.theme.ComposePlayerTheme
import com.hd1998.composeplayer.presentation.videoList.VideoList
import org.koin.androidx.compose.koinViewModel

const val REQUEST_CODE = 1

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
                REQUEST_CODE
            )
        }
        enableEdgeToEdge()
        setContent {
            ComposePlayerTheme {
                val viewModel = koinViewModel<MainViewModel>()
                viewModel.loadVideo()
                val list = viewModel.videoStateFlow.collectAsState()
                val loading = viewModel.loading.collectAsState()
                Surface {
                    VideoList(list = list, loading)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposePlayerTheme {
        Greeting("Android")
    }
}