package com.hd1998.composeplayer.di

import androidx.work.WorkerFactory
import com.hd1998.composeplayer.data.worker.MyWorkerFactory
import org.koin.dsl.module

val workerFactoryModule = module {
    factory<WorkerFactory> { MyWorkerFactory(get()) }
}