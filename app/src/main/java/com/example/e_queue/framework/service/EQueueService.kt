package com.example.e_queue.framework.service

import com.example.e_queue.app.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EQueueService {

    //Users
    @GET("operator/getUsers")
    suspend fun getUsers(): List<User>

    @GET("operator/getNextCustomerInfo")
    suspend fun getNextCustomerInfo(@Query("user_id") userId: Int): NextCustomerInfo

    @GET("operator/inviteNextCustomer")
    suspend fun inviteNextCustomer(@Query("user_id") userId: Int): InviteNextCustomerInfo

    @GET("operator/killNextCustomer")
    suspend fun killNextCustomer(@Query("user_id") userId: Int)

    @GET("operator/getStartCustomer")
    suspend fun getStartCustomer(@Query("user_id") userId: Int)

    @GET("operator/getSelfServices")
    suspend fun getSelfServices(@Query("user_id") userId: Int): ServicesLength

    @GET("terminal/getServices")
    suspend fun getServices(): ServicesList

    @POST("operator/redirectCustomer")
    suspend fun redirectCustomer(@Body customer: BodyForRedirectCustomer)

    @GET("operator/getResultsList")
    suspend fun getResultsList(): ResultList

    @POST("operator/getFinishCustomer")
    suspend fun finishWorkWithCustomer(@Body result: BodyForFinishWorkWithCustomer)

    @POST("operator/customerToPostpone")
    suspend fun customerToPostpone(@Body customer: BodyForPostponedCustomer)

    @GET("operator/getPostponedPoolInfo")
    suspend fun getPostponedPoolInfo(): List<InviteNextCustomerInfo>

    @GET("operator/invitePostponeCustomer")
    suspend fun invitePostponedCustomer(
        @Query("user_id") userId: Int,
        @Query("customer_id") customerId: Long
    ): InviteNextCustomerInfo

    //common
    @GET("common/health")
    suspend fun checkHealth()

}
