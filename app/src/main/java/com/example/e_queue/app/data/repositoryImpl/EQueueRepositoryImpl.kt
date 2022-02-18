package com.example.e_queue.app.data.repositoryImpl

import com.example.e_queue.app.data.model.*
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.framework.remote.RemoteDataSource

class EQueueRepositoryImpl(private val dataSource: RemoteDataSource): EQueueRepository {
    override suspend fun getUsers(): List<User> {
        return dataSource.retrofit.getUsers()
    }

    override suspend fun getNextCustomerInfo(userId: Int): NextCustomerInfo {
        return dataSource.retrofit.getNextCustomerInfo(userId)
    }

    override suspend fun inviteNextCustomer(userId: Int): InviteNextCustomerInfo {
        return dataSource.retrofit.inviteNextCustomer(userId)
    }

    override suspend fun killNextCustomer(userId: Int) {
        dataSource.retrofit.killNextCustomer(userId)
    }

    override suspend fun getStartCustomer(userId: Int) {
        dataSource.retrofit.getStartCustomer(userId)
    }

    override suspend fun getSelfServices(userId: Int): ServicesLength {
        return  dataSource.retrofit.getSelfServices(userId)
    }

    override suspend fun getServices(): ServicesList {
       return dataSource.retrofit.getServices()
    }

    override suspend fun redirectCustomer(customer: BodyForRedirectCustomer) {
        dataSource.retrofit.redirectCustomer(customer)
    }

    override suspend fun getResultsList(): ResultList {
        return dataSource.retrofit.getResultsList()
    }

    override suspend fun finishWorkWithCustomer(result: BodyForFinishWorkWithCustomer) {
        dataSource.retrofit.finishWorkWithCustomer(result)
    }

    override suspend fun customerToPostpone(customer: BodyForPostponedCustomer) {
        dataSource.retrofit.customerToPostpone(customer)
    }

    override suspend fun getPostponedPoolInfo(): List<InviteNextCustomerInfo> {
        return dataSource.retrofit.getPostponedPoolInfo()
    }

    override suspend fun invitePostponedCustomer(
        userId: Int,
        customerId: Long
    ): InviteNextCustomerInfo {
        return dataSource.retrofit.invitePostponedCustomer(userId, customerId)
    }

    override suspend fun checkHealth() {
        dataSource.retrofit.checkHealth()
    }

}