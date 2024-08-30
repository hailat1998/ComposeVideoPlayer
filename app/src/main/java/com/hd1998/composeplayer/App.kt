package com.hd1998.composeplayer

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.hd1998.composeplayer.data.worker.MyWorkerFactory
import com.hd1998.composeplayer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import java.util.concurrent.Executors

class App : Application(), KoinComponent, Configuration.Provider {

            private val myWorkerFactory: MyWorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(appModule)
        }
    }

    override val workManagerConfiguration: Configuration
        get() {
            val workerFactory = DelegatingWorkerFactory()
            workerFactory.addFactory(myWorkerFactory)
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .setExecutor(Executors.newFixedThreadPool(4))
                .setMinimumLoggingLevel(Log.INFO)
                .build()
        }

}