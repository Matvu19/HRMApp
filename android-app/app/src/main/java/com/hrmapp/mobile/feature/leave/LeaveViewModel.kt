package com.hrmapp.mobile.feature.leave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.LeaveApi
import com.hrmapp.mobile.core.network.LeaveCreateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(
    private val leaveApi: LeaveApi
) : ViewModel() {

    data class UiState(
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun submit(
        dateFrom: String,
        dateTo: String,
        reason: String
    ) {
        viewModelScope.launch {
            try {
                val response = leaveApi.create(
                    LeaveCreateRequest(
                        employeeId = 1L,
                        leaveTypeId = 1L,
                        dateFrom = dateFrom,
                        dateTo = dateTo,
                        durationDays = 2.0,
                        reasonText = reason
                    )
                )

                _uiState.value = UiState(
                    message = "Đã gửi đơn nghỉ #${response.data?.leaveRequestId ?: ""}"
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    message = e.message ?: "Không gửi được đơn nghỉ"
                )
            }
        }
    }
}