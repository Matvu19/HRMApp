package com.hrmapp.mobile.core.network

import retrofit2.http.GET
import retrofit2.http.Path

data class ManagerDashboardData(
    val managerEmployeeId: Long,
    val pendingApprovalCount: Int,
    val todayCheckinCount: Int,
    val unreadNotificationCount: Int,
    val teamSize: Int
)

interface DashboardApi {
    @GET("api/dashboard/manager/{managerEmployeeId}")
    suspend fun getManagerDashboard(
        @Path("managerEmployeeId") managerEmployeeId: Long
    ): ApiResponse<ManagerDashboardData>
}