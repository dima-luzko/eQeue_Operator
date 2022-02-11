package com.example.e_queue.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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

data class ResultList(
    @SerializedName("result")
    val result: List<Result>
)

data class Result(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class ServicesList(
    @SerializedName("inner_services")
    val innerServices: List<InnerServicesForServiceList>
)

data class InnerServicesForServiceList(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)

data class NextCustomerInfo(
    @SerializedName("number")
    val number: String,
    @SerializedName("prefix")
    val prefix: String
)

data class InviteNextCustomerInfo(
    @SerializedName("id")
    val id: Long,
    @SerializedName("number")
    val number: String?,
    @SerializedName("to_service")
    val serviceName: InviteNextCustomerServiceInfo,
    @SerializedName("prefix")
    val prefix: String?,
    @SerializedName("post_status")
    val comments: String?
)

data class InviteNextCustomerServiceInfo(
    @SerializedName("name")
    val name: String?,
    @SerializedName("result_required")
    val resultRequired: Boolean
)

data class ServicesLength(
    @SerializedName("self_services")
    val selfServices: List<SelfServices>
)

data class BodyForRedirectCustomer(
    @SerializedName("service_id")
    val serviceId: Long,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("comments")
    val comments: String?,
    @SerializedName("result_id")
    val resultId: Int,
    @SerializedName("request_back")
    val requestBack: Boolean
)

data class BodyForFinishWorkWithCustomer(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("result_id")
    val resultId: Int
)

data class SelfServices(
    @SerializedName("id")
    val id: Long,
    @SerializedName("service_name")
    val serviceName: String,
    @SerializedName("line")
    val line: List<ServiceInfoForServiceLength>
)

data class ServiceInfoForServiceLength(
    @SerializedName("id")
    val id: Long,
    @SerializedName("number")
    val number: String,
)

data class BodyForPostponedCustomer(
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("comments")
    val comments: String?,
    @SerializedName("is_only_mine")
    val isOnlyMine: Boolean,
    @SerializedName("postponed_period")
    val postponedPeriod: Int
)

@Parcelize
data class SelectedUser(
    val id: Int,
    val name: String,
    val point: String,
    val password: String
) : Parcelable

@Parcelize
data class SelectedServices(
    val id: Long,
    val name: String
) : Parcelable

@Parcelize
data class LoggedUser(
    val id: Int,
    val name: String,
    val point: String
) : Parcelable

@Parcelize
data class OperationWithLoggedUser(
    val userId: Int,
    val userName: String,
    val point: String,
    val clientNumber: String?
) : Parcelable

@Parcelize
data class InvitePostponedClient(
    val number: String?,
    val serviceName: String?,
    val prefix: String?,
    val resultRequired: Boolean
) : Parcelable
