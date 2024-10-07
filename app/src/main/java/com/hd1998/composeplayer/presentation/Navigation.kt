package com.hd1998.composeplayer.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hd1998.composeplayer.presentation.player.Player
import com.hd1998.composeplayer.presentation.videoList.VideoList
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainNavHost(navHostController: NavHostController){1
    val viewModel = koinViewModel<MainViewModel>()
    NavHost(navController = navHostController, startDestination = Routes.Home) {
        composable<Routes.Home> {
            viewModel.loadVideo()
            val list = viewModel.videoStateFlow.collectAsStateWithLifecycle()
            val querying = viewModel.loading.collectAsStateWithLifecycle()
            VideoList(list = list, querying = querying, toPlayer = { uri ->
                navHostController.navigate(Routes.Player(uri))
            }, incrementPlayed = viewModel::incrementPlayed )
        }

        composable<Routes.Player> {backStackEntry ->
            val arg = backStackEntry.toRoute<Routes.Player>()
            Player(uri = arg.uri)
        }
    }
}