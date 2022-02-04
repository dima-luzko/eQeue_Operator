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
}