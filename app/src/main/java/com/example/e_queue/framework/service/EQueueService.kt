package com.example.e_queue.framework.service

import com.example.e_queue.app.data.model.*
import retrofit2.http.*

interface EQueueService {


    // user
    @GET
    suspend fun getUsers(@Url url: String): List<User>

    @GET
    suspend fun getNextCustomerInfo(
        @Url url: String,
        @Query("user_id") userId: Int
    ): NextCustomerInfo

    @GET
    suspend fun inviteNextCustomer(
        @Url url: String,
        @Query("user_id") userId: Int
    ): InviteNextCustomerInfo

    @GET
    suspend fun killNextCustomer(@Url url: String, @Query("user_id") userId: Int)

    @GET
    suspend fun getStartCustomer(@Url url: String, @Query("user_id") userId: Int)

    @GET
    suspend fun getSelfServices(@Url url: String, @Query("user_id") userId: Int): ServicesLength

    @GET
    suspend fun getServices(@Url url: String): ServicesList

    @POST
    suspend fun redirectCustomer(@Url url: String, @Body customer: BodyForRedirectCustomer)

    @GET
    suspend fun getResultsList(@Url url: String): ResultList

    @POST
    suspend fun finishWorkWithCustomer(
        @Url url: String,
        @Body result: BodyForFinishWorkWithCustomer
    )

    @POST
    suspend fun customerToPostpone(@Url url: String, @Body customer: BodyForPostponedCustomer)

    @GET
    suspend fun getPostponedPoolInfo(@Url url: String): List<InviteNextCustomerInfo>

    @GET
    suspend fun invitePostponedCustomer(
        @Url url: String,
        @Query("user_id") userId: Int,
        @Query("customer_id") customerId: Long
    ): InviteNextCustomerInfo

    // common
    @GET
    suspend fun checkHealth(@Url url: String)


}
