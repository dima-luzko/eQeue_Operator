package com.example.e_queue

import android.app.Application
import com.example.e_queue.app.presentation.di.dataSourceModules
import com.example.e_queue.app.presentation.di.repositoryModules
import com.example.e_queue.app.presentation.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(dataSourceModules, viewModelModules, repositoryModules))
        }
    }
}