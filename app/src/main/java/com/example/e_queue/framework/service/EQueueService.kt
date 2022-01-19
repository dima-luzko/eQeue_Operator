package com.example.e_queue.framework.service

import com.example.e_queue.app.data.model.*
import retrofit2.http.GET

interface EQueueService {

    //Users
    @GET("getUsers")
    suspend fun getUsers(): List<User>

}