package com.example.e_queue.app.presentation.di

import com.example.e_queue.app.data.repositoryImpl.EQueueRepositoryImpl
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.app.presentation.viewModel.EQueueViewModel
import com.example.e_queue.framework.remote.RemoteDataSource
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val dataSourceModules = module {
    single { RemoteDataSource() }
}

val viewModelModules = module {
    viewModel { EQueueViewModel(get()) }
}

val repositoryModules = module {
    single<EQueueRepository> { EQueueRepositoryImpl(get()) }
}