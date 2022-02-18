package com.example.e_queue.app.domain.repository

import com.example.e_queue.app.data.model.*

interface EQueueRepository {
    suspend fun getUsers(): List<User>
    suspend fun getNextCustomerInfo(userId: Int): NextCustomerInfo
    suspend fun inviteNextCustomer(userId: Int): InviteNextCustomerInfo
    suspend fun killNextCustomer(userId: Int)
    suspend fun getStartCustomer(userId: Int)
    suspend fun getSelfServices(userId: Int): ServicesLength
    suspend fun getServices(): ServicesList
    suspend fun redirectCustomer(customer: BodyForRedirectCustomer)
    suspend fun getResultsList(): ResultList
    suspend fun finishWorkWithCustomer(result: BodyForFinishWorkWithCustomer)
    suspend fun customerToPostpone(customer: BodyForPostponedCustomer)
    suspend fun getPostponedPoolInfo(): List<InviteNextCustomerInfo>
    suspend fun invitePostponedCustomer(userId: Int, customerId: Long): InviteNextCustomerInfo
    suspend fun checkHealth()
}