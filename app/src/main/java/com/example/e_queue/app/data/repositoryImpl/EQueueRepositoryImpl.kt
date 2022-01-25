package com.example.e_queue.app.data.repositoryImpl

import com.example.e_queue.app.data.model.NextCustomerInfo
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.data.model.UserServiceLength
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.framework.remote.RemoteDataSource

class EQueueRepositoryImpl(private val dataSource: RemoteDataSource): EQueueRepository {
    override suspend fun getUsers(): List<User> {
        return dataSource.retrofit.getUsers()
    }

    override suspend fun getUserServiceLength(userServiceId: Long?): UserServiceLength {
        return dataSource.retrofit.getUserServiceLength(userServiceId!!)
    }

    override suspend fun getNextCustomerInfo(userId: Int): NextCustomerInfo {
        return dataSource.retrofit.getNextCustomerInfo(userId)
    }

}