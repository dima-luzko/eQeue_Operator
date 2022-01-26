package com.example.e_queue.framework.service

import com.example.e_queue.app.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EQueueService {

    //Users
    @GET("operator/getUsers")
    suspend fun getUsers(): List<User>

    @GET("terminal/aboutService")
    suspend fun getUserServiceLength(@Query("service_id") userServiceId: Long): UserServiceLength

    @GET("operator/getNextCustomerInfo")
    suspend fun getNextCustomerInfo(@Query("user_id") userId: Int): NextCustomerInfo

    @GET("operator/inviteNextCustomer")
    suspend fun inviteNextCustomer(@Query("user_id") userId: Int) : InviteNextCustomerInfo

    @GET("operator/killNextCustomer")
    suspend fun killNextCustomer(@Query("user_id") userId: Int)


}