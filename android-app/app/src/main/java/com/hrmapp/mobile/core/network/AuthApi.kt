package com.hrmapp.mobile.core.network

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginData(
    val userId: Long,
    val employeeId: Long,
    val username: String,
    val roleCode: String,
    val accessToken: String,
    val refreshToken: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginData>
}