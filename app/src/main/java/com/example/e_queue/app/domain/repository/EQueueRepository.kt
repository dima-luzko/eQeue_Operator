package com.example.e_queue.app.domain.repository

import com.example.e_queue.app.data.model.User

interface EQueueRepository {
    suspend fun getUsers(): List<User>
}