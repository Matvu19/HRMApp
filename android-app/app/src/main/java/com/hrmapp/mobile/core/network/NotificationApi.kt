package com.hrmapp.mobile.core.network

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class NotificationItem(
    val notificationEventId: Long,
    val eventCode: String,
    val recipientUserId: Long,
    val title: String,
    val messageText: String,
    val deeplinkUrl: String,
    val pushStatus: String,
    val sentAt: String?,
    val readAt: String?,
    val status: String
)

interface NotificationApi {
    @GET("api/notifications/user/{userId}")
    suspend fun getNotifications(
        @Path("userId") userId: Long
    ): ApiResponse<List<NotificationItem>>

    @POST("api/notifications/{notificationId}/read")
    suspend fun markRead(
        @Path("notificationId") notificationId: Long
    ): ApiResponse<NotificationItem>
}