package com.hrmapp.mobile.core.network

import retrofit2.http.GET
import retrofit2.http.Path

data class PayrollItem(
    val payrollId: Long,
    val employeeId: Long,
    val payrollMonth: String,
    val grossSalary: Double,
    val allowanceAmount: Double,
    val deductionAmount: Double,
    val netSalary: Double,
    val status: String
)

interface PayrollApi {
    @GET("api/payroll/employee/{employeeId}")
    suspend fun getPayrollsByEmployee(
        @Path("employeeId") employeeId: Long
    ): ApiResponse<List<PayrollItem>>
}