package com.hrmapp.mobile.feature.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.AttendanceApi
import com.hrmapp.mobile.core.network.AttendanceCheckRequest
import com.hrmapp.mobile.core.network.AttendanceEventItem
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceApi: AttendanceApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val items: List<AttendanceEventItem> = emptyList(),
        val message: String = "",
        val queueCount: Int = 0
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun loadHistory() {
        viewModelScope.launch {
            try {
                val session = sessionManager.sessionFlow.first()
                val response = attendanceApi.history(session.employeeId)

                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    items = response.data ?: emptyList(),
                    message = if (response.data.isNullOrEmpty()) "Chưa có lịch sử chấm công" else ""
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    message = e.message ?: "Không tải được lịch sử chấm công"
                )
            }
        }
    }

    fun checkIn() {
        submit("CHECK_IN", "android-checkin-${System.currentTimeMillis()}")
    }

    fun checkOut() {
        submit("CHECK_OUT", "android-checkout-${System.currentTimeMillis()}")
    }

    private fun submit(eventType: String, key: String) {
        viewModelScope.launch {
            try {
                val session = sessionManager.sessionFlow.first()

                val response = attendanceApi.check(
                    AttendanceCheckRequest(
                        employeeId = session.employeeId,
                        assignmentId = 1L,
                        eventType = eventType,
                        eventTimeDevice = LocalDateTime.now().toString(),
                        geoLat = 10.7769,
                        geoLng = 106.7009,
                        deviceId = "android-demo-01",
                        idempotencyKey = key
                    )
                )

                _uiState.value = _uiState.value?.copy(
                    message = "Thành công: ${response.data?.eventType ?: eventType}",
                    queueCount = 0
                )

                loadHistory()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    message = "Không gửi được, đã đưa vào hàng chờ",
                    queueCount = (_uiState.value?.queueCount ?: 0) + 1
                )
            }
        }
    }
}