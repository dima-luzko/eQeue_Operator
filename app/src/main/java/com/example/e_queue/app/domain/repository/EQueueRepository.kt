package com.example.e_queue.app.domain.repository

import com.example.e_queue.app.data.model.*

interface EQueueRepository {
    suspend fun getUsers(url: String): List<User>
    suspend fun getNextCustomerInfo(url: String, userId: Int): NextCustomerInfo
    suspend fun inviteNextCustomer(url: String, userId: Int): InviteNextCustomerInfo
    suspend fun killNextCustomer(url: String, userId: Int)
    suspend fun getStartCustomer(url: String, userId: Int)
    suspend fun getSelfServices(url: String, userId: Int): ServicesLength
    suspend fun getServices(url: String): ServicesList
    suspend fun redirectCustomer(url: String, customer: BodyForRedirectCustomer)
    suspend fun getResultsList(url: String): ResultList
    suspend fun finishWorkWithCustomer(url: String, result: BodyForFinishWorkWithCustomer)
    suspend fun customerToPostpone(url: String, customer: BodyForPostponedCustomer)
    suspend fun getPostponedPoolInfo(url: String): List<InviteNextCustomerInfo>
    suspend fun invitePostponedCustomer(
        url: String,
        userId: Int,
        customerId: Long
    ): InviteNextCustomerInfo

    suspend fun checkHealth(url: String)
}