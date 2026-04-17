package com.hrmapp.mobile.core.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class ApprovalStepItem(
    val approvalStepId: Long,
    val approvalFlowId: Long,
    val stepNo: Int,
    val approverEmployeeId: Long,
    val approverRoleCode: String,
    val decision: String?,
    val decisionNote: String?,
    val actedAt: String?,
    val status: String
)

data class ApprovalActionRequest(
    val approvalStepId: Long,
    val decision: String,
    val decisionNote: String
)

interface ApprovalApi {
    @GET("api/approvals/pending/{approverEmployeeId}")
    suspend fun getPending(
        @Path("approverEmployeeId") approverEmployeeId: Long
    ): ApiResponse<List<ApprovalStepItem>>

    @POST("api/approvals/action")
    suspend fun action(
        @Body request: ApprovalActionRequest
    ): ApiResponse<ApprovalStepItem>
}