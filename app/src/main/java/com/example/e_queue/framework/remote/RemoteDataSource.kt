package com.example.e_queue.framework.remote

import com.example.e_queue.MainApplication
import com.example.e_queue.framework.service.EQueueService
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteDataSource {
    private val gson = GsonBuilder().create()
    val retrofit: EQueueService = Retrofit.Builder()
//        .baseUrl(
//            "http://${
//                PreferencesManager.getInstance(MainApplication().getAppContext())
//                    .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
//            }/api/"
//        )
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(getHttpClient())
        .build()
        .create(EQueueService::class.java)

    private fun getHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15L, TimeUnit.SECONDS)
        return builder.build()
    }
}