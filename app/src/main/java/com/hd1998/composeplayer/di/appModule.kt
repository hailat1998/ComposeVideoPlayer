package com.hd1998.composeplayer.di

import androidx.room.Room
import androidx.work.WorkManager
import com.hd1998.composeplayer.data.local.database.VideoDatabase
import com.hd1998.composeplayer.data.reposirory.VideoRepositoryImp
import com.hd1998.composeplayer.data.worker.VideoWorker
import com.hd1998.composeplayer.domain.model.Video
import com.hd1998.composeplayer.domain.repository.Repository
import com.hd1998.composeplayer.presentation.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

const val DB = "videoDB"

val appModule = module{

    single{
        Room.databaseBuilder(
            androidApplication(),
            VideoDatabase::class.java,
            DB
        ).build()
    }

    single<WorkManager> { WorkManager.getInstance(androidApplication()) }

    single<Repository>{VideoRepositoryImp(get(), get())}

    worker(named<VideoWorker>()){ VideoWorker(get(), androidContext(), get())}

    viewModel{MainViewModel(get())}

}