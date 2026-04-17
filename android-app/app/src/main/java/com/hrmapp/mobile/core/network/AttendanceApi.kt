package com.hrmapp.mobile.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class AttendanceCheckRequest(
    val employeeId: Long,
    val assignmentId: Long,
    val eventType: String,
    val eventTimeDevice: String,
    val geoLat: Double,
    val geoLng: Double,
    val deviceId: String,
    val idempotencyKey: String
)

data class AttendanceEventItem(
    val eventId: Long,
    val employeeId: Long,
    val assignmentId: Long,
    val eventType: String,
    val eventTimeDevice: String,
    val eventTimeServer: String,
    val geoLat: Double?,
    val geoLng: Double?,
    val status: String
)

interface AttendanceApi {
    @POST("api/attendance/check")
    suspend fun check(
        @Body request: AttendanceCheckRequest
    ): ApiResponse<AttendanceEventItem>

    @GET("api/attendance/history/{employeeId}")
    suspend fun history(
        @Path("employeeId") employeeId: Long
    ): ApiResponse<List<AttendanceEventItem>>
}