package com.example.e_queue.app.data.repositoryImpl

import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.framework.remote.RemoteDataSource

class EQueueRepositoryImpl(private val dataSource: RemoteDataSource): EQueueRepository {
    override suspend fun getUsers(): List<User> {
        return dataSource.retrofit.getUsers()
    }

}