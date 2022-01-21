package com.example.e_queue.app.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("point")
    val point: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("plan")
    val plan: List<Plan>?
)

data class Plan(
    @SerializedName("id")
    val id: Int,
    @SerializedName("coeff")
    val coeff: Int,
    @SerializedName("flex")
    val flex: Boolean,
    @SerializedName("flex_invt")
    val flexInvite: Boolean,
    @SerializedName("service")
    val service: Service
)

data class Service(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

data class SelectedUser(
    val id: Int,
    val name: String,
    val point: String,
    val password: String
) : Serializable

data class LoggedUser(
    val id: Int,
    val name: String,
    val point: String
) : Serializable

@kotlinx.serialization.Serializable
data class Message(
    @SerializedName("self_services")
    val selfServices: List<SelfServices>
)

data class SelfServices(
     @SerializedName("id")
    val id : Int,
    @SerializedName("service_name")
    val serviceName: String
)


