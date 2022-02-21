package com.example.e_queue.app.data.repositoryImpl

import com.example.e_queue.app.data.model.*
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.framework.remote.RemoteDataSource

class EQueueRepositoryImpl(private val dataSource: RemoteDataSource) : EQueueRepository {

    //user
    override suspend fun getUsers(url: String): List<User> {
        return dataSource.retrofit.getUsers(url)
    }

    override suspend fun getNextCustomerInfo(url: String, userId: Int): NextCustomerInfo {
        return dataSource.retrofit.getNextCustomerInfo(url, userId)
    }

    override suspend fun inviteNextCustomer(url: String, userId: Int): InviteNextCustomerInfo {
        return dataSource.retrofit.inviteNextCustomer(url, userId)
    }

    override suspend fun killNextCustomer(url: String, userId: Int) {
        dataSource.retrofit.killNextCustomer(url, userId)
    }

    override suspend fun getStartCustomer(url: String, userId: Int) {
        dataSource.retrofit.getStartCustomer(url, userId)
    }

    override suspend fun getSelfServices(url: String, userId: Int): ServicesLength {
        return dataSource.retrofit.getSelfServices(url, userId)
    }

    override suspend fun getServices(url: String): ServicesList {
        return dataSource.retrofit.getServices(url)
    }

    override suspend fun redirectCustomer(url: String, customer: BodyForRedirectCustomer) {
        dataSource.retrofit.redirectCustomer(url, customer)
    }

    override suspend fun getResultsList(url: String): ResultList {
        return dataSource.retrofit.getResultsList(url)
    }

    override suspend fun finishWorkWithCustomer(
        url: String,
        result: BodyForFinishWorkWithCustomer
    ) {
        dataSource.retrofit.finishWorkWithCustomer(url, result)
    }

    override suspend fun customerToPostpone(url: String, customer: BodyForPostponedCustomer) {
        dataSource.retrofit.customerToPostpone(url, customer)
    }

    override suspend fun getPostponedPoolInfo(url: String): List<InviteNextCustomerInfo> {
        return dataSource.retrofit.getPostponedPoolInfo(url)
    }

    override suspend fun invitePostponedCustomer(
        url: String,
        userId: Int,
        customerId: Long
    ): InviteNextCustomerInfo {
        return dataSource.retrofit.invitePostponedCustomer(url, userId, customerId)
    }

    //common
    override suspend fun checkHealth(url: String) {
        dataSource.retrofit.checkHealth(url)
    }
}