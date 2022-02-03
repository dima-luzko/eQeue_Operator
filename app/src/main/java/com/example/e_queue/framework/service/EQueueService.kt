package com.example.e_queue.framework.service

import com.example.e_queue.app.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EQueueService {

    //Users
    @GET("operator/getUsers")
    suspend fun getUsers(): List<User>

    @GET("operator/getNextCustomerInfo")
    suspend fun getNextCustomerInfo(@Query("user_id") userId: Int): NextCustomerInfo

    @GET("operator/inviteNextCustomer")
    suspend fun inviteNextCustomer(@Query("user_id") userId: Int) : InviteNextCustomerInfo

    @GET("operator/killNextCustomer")
    suspend fun killNextCustomer(@Query("user_id") userId: Int)

    @GET("operator/getStartCustomer")
    suspend fun getStartCustomer(@Query("user_id") userId: Int)

    @GET("operator/getSelfServices")
    suspend fun getSelfServices(@Query("user_id") userId: Int) : ServicesLength

}