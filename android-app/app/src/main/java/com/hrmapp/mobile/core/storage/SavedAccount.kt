package com.hrmapp.mobile.core.storage

data class SavedAccount(
    val username: String,
    val roleCode: String,
    val employeeId: Long,
    val userId: Long
)