package com.example.e_queue.app.presentation.di

import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.data.repositoryImpl.EQueueRepositoryImpl
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.app.presentation.viewModel.LoggedUserViewModel
import com.example.e_queue.app.presentation.viewModel.SelectedUserViewModel
import com.example.e_queue.app.presentation.viewModel.UserListViewModel
import com.example.e_queue.app.presentation.viewModel.OperationWithLoggedUserViewModel
import com.example.e_queue.framework.remote.RemoteDataSource
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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
        )
    }

    viewModel { (modelUser: OperationWithLoggedUser) ->
        OperationWithLoggedUserViewModel(
            operationWithUser = modelUser,
            eQueueRepository = get()
        )
    }
}

val repositoryModules = module {
    single<EQueueRepository> { EQueueRepositoryImpl(get()) }
}