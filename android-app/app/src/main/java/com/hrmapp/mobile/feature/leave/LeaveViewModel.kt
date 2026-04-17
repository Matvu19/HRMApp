package com.hrmapp.mobile.feature.leave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.LeaveApi
import com.hrmapp.mobile.core.network.LeaveCreateRequest
import com.hrmapp.mobile.core.network.LeaveItem
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(
    private val leaveApi: LeaveApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val items: List<LeaveItem> = emptyList(),
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun loadHistory() {
        viewModelScope.launch {
            try {
                val session = sessionManager.sessionFlow.first()
                val response = leaveApi.myRequests(session.employeeId)

                _uiState.value = _uiState.value?.copy(
                    items = response.data ?: emptyList(),
                    message = if (response.data.isNullOrEmpty()) "Chưa có đơn nghỉ nào" else ""
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    message = e.message ?: "Không tải được lịch sử đơn nghỉ"
                )
            }
        }
    }

    fun submit(
        dateFrom: String,
        dateTo: String,
        reason: String
    ) {
        viewModelScope.launch {
            try {
                val session = sessionManager.sessionFlow.first()

                val response = leaveApi.create(
                    LeaveCreateRequest(
                        employeeId = session.employeeId,
                        leaveTypeId = 1L,
                        dateFrom = dateFrom,
                        dateTo = dateTo,
                        durationDays = 2.0,
                        reasonText = reason
                    )
                )

                _uiState.value = _uiState.value?.copy(
                    message = "Đã gửi đơn nghỉ #${response.data?.leaveRequestId ?: ""}"
                )

                loadHistory()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    message = e.message ?: "Không gửi được đơn nghỉ"
                )
            }
        }
    }
}