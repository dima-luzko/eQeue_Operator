package com.example.e_queue.app.presentation.di

import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.repositoryImpl.EQueueRepositoryImpl
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.app.presentation.viewModel.UserListViewModel
import com.example.e_queue.app.presentation.viewModel.LoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.SelectedUserViewModel
import com.example.e_queue.framework.remote.RemoteDataSource
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val dataSourceModules = module {
    single { RemoteDataSource() }
}

val viewModelModules = module {
    viewModel { UserListViewModel(get()) }
    viewModel { SelectedUserViewModel() }
    viewModel { (model: LoggedUser) ->
        LoggedUserViewModel(
            loggedUserModel = model,
            eQueueRepository = get()
        ) }
}

val repositoryModules = module {
    single<EQueueRepository> { EQueueRepositoryImpl(get()) }
}