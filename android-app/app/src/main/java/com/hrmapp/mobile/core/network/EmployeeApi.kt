package com.hrmapp.mobile.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class EmployeeProfileData(
    val employeeId: Long,
    val employeeCode: String,
    val fullName: String,
    val dob: String?,
    val gender: String?,
    val workEmail: String?,
    val personalPhone: String?,
    val orgUnitId: Long?,
    val positionId: Long?,
    val managerEmployeeId: Long?,
    val joinDate: String?,
    val avatarUrl: String?,
    val status: String?
)

data class UpdateEmployeeProfileRequest(
    val fullName: String,
    val personalPhone: String?,
    val gender: String?
)

interface EmployeeApi {
    @GET("api/employees/{employeeId}")
    suspend fun getEmployeeById(
        @Path("employeeId") employeeId: Long
    ): ApiResponse<EmployeeProfileData>

    @PUT("api/employees/{employeeId}")
    suspend fun updateEmployeeById(
        @Path("employeeId") employeeId: Long,
        @Body request: UpdateEmployeeProfileRequest
    ): ApiResponse<EmployeeProfileData>
}