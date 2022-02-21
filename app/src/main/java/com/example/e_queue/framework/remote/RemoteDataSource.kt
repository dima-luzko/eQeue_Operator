package com.example.e_queue.framework.remote

import com.example.e_queue.framework.service.EQueueService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RemoteDataSource {
    private var apiBaseUrl = "http://localhost"

    private val gson = GsonBuilder().create()

    val retrofit: EQueueService = Retrofit.Builder()
        .baseUrl(apiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(getHttpClient())
        .build()
        .create(EQueueService::class.java)

    private fun getHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(5L, TimeUnit.SECONDS)
        return builder.build()
    }
}