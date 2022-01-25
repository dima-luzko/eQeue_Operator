package com.example.e_queue.app.domain.repository

import com.example.e_queue.app.data.model.NextCustomerInfo
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.data.model.UserServiceLength

interface EQueueRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserServiceLength(userServiceId: Long?): UserServiceLength
    suspend fun getNextCustomerInfo(userId: Int): NextCustomerInfo
}