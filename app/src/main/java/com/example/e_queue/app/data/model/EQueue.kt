package com.example.e_queue.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
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
    val plan: List<UserPlan>?
)

data class UserPlan(
    @SerializedName("id")
    val id: Int,
    @SerializedName("coeff")
    val coeff: Int,
    @SerializedName("flex")
    val flex: Boolean,
    @SerializedName("flex_invt")
    val flexInvite: Boolean,
    @SerializedName("service")
    val service: PlanService
)

data class PlanService(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

data class UserServiceLength(
    @SerializedName("length_line")
    val length: Int
)

@Parcelize
data class SelectedUser(
    val id: Int,
    val name: String,
    val point: String,
    val password: String,
    val service_id: Long?
) : Parcelable

data class LoggedUser(
    val id: Int,
    val name: String,
    val point: String,
    val service_id: Long?
) : Serializable



