package com.hrmapp.mobile.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LeaveCreateRequest(
    val employeeId: Long,
    val leaveTypeId: Long,
    val dateFrom: String,
    val dateTo: String,
    val durationDays: Double,
    val reasonText: String
)

data class LeaveItem(
    val leaveRequestId: Long,
    val employeeId: Long,
    val leaveTypeId: Long,
    val approvalFlowId: Long?,
    val dateFrom: String,
    val dateTo: String,
    val durationDays: Double,
    val reasonText: String?,
    val status: String
)

interface LeaveApi {
    @POST("api/leave/requests")
    suspend fun create(
        @Body request: LeaveCreateRequest
    ): ApiResponse<LeaveItem>

    @GET("api/leave/requests/{employeeId}")
    suspend fun myRequests(
        @Path("employeeId") employeeId: Long
    ): ApiResponse<List<LeaveItem>>
}