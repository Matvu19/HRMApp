package com.hrmapp.mobile.core.network

import retrofit2.http.Body
import retrofit2.http.POST

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

data class SimpleMessageResponse(
    val success: Boolean,
    val message: String?
)

interface AccountApi {
    @POST("api/account/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): SimpleMessageResponse
}