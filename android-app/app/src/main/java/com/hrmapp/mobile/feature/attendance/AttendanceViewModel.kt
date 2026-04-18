package com.hrmapp.mobile.feature.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.location.SimpleLocation
import com.hrmapp.mobile.core.network.AttendanceApi
import com.hrmapp.mobile.core.network.AttendanceCheckRequest
import com.hrmapp.mobile.core.network.AttendanceEventItem
import com.hrmapp.mobile.core.storage.AttendanceQueueStore
import com.hrmapp.mobile.core.storage.QueuedAttendanceAction
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceApi: AttendanceApi,
    private val sessionManager: SessionManager,
    private val queueStore: AttendanceQueueStore
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val items: List<AttendanceEventItem> = emptyList(),
        val message: String = "",
        val queueCount: Int = 0,
        val locationText: String = "Chưa lấy được tọa độ",
        val gpsStatus: String = "Vị trí: Chưa cấp quyền"
    )

    private val _uiState = MutableLiveData(
        UiState(queueCount = queueStore.size())
    )
    val uiState: LiveData<UiState> = _uiState

    fun updateLocation(location: SimpleLocation?) {
        _uiState.value = _uiState.value?.copy(
            locationText = if (location != null) {
                "Vĩ độ: ${location.latitude}, Kinh độ: ${location.longitude}"
            } else {
                "Chưa lấy được tọa độ"
            },
            gpsStatus = if (location != null) {
                "Vị trí: Sẵn sàng"
            } else {
                "Vị trí: Chưa sẵn sàng"
            }
        )
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(
                isLoading = true,
                queueCount = queueStore.size()
            )

            try {
                val session = sessionManager.sessionFlow.first()
                val response = attendanceApi.history(session.employeeId)
                val items = response.data ?: emptyList()

                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    items = items,
                    message = if (items.isEmpty()) "Chưa có lịch sử chấm công" else "",
                    queueCount = queueStore.size()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    items = emptyList(),
                    message = e.message ?: "Không tải được lịch sử chấm công",
                    queueCount = queueStore.size()
                )
            }
        }
    }

    fun checkIn(location: SimpleLocation?) {
        submit("CHECK_IN", "android-checkin-${System.currentTimeMillis()}", location)
    }

    fun checkOut(location: SimpleLocation?) {
        submit("CHECK_OUT", "android-checkout-${System.currentTimeMillis()}", location)
    }

    fun retryQueuedActions() {
        viewModelScope.launch {
            val queued = queueStore.getAll()
            if (queued.isEmpty()) {
                _uiState.value = _uiState.value?.copy(
                    message = "Không có thao tác nào trong hàng chờ",
                    queueCount = 0
                )
                return@launch
            }

            _uiState.value = _uiState.value?.copy(isLoading = true)

            val failed = mutableListOf<QueuedAttendanceAction>()

            for (item in queued) {
                try {
                    attendanceApi.check(
                        AttendanceCheckRequest(
                            employeeId = item.employeeId,
                            assignmentId = item.assignmentId,
                            eventType = item.eventType,
                            eventTimeDevice = item.eventTimeDevice,
                            geoLat = item.geoLat,
                            geoLng = item.geoLng,
                            deviceId = item.deviceId,
                            idempotencyKey = item.idempotencyKey
                        )
                    )
                } catch (e: Exception) {
                    failed.add(item)
                }
            }

            queueStore.saveAll(failed)

            _uiState.value = _uiState.value?.copy(
                isLoading = false,
                message = if (failed.isEmpty()) {
                    "Đã gửi lại toàn bộ hàng chờ thành công"
                } else {
                    "Còn ${failed.size} thao tác chưa gửi được"
                },
                queueCount = queueStore.size()
            )

            loadHistory()
        }
    }

    private fun submit(
        eventType: String,
        key: String,
        location: SimpleLocation?
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isLoading = true)

            try {
                val session = sessionManager.sessionFlow.first()

                val lat = location?.latitude ?: 10.7769
                val lng = location?.longitude ?: 106.7009

                val response = attendanceApi.check(
                    AttendanceCheckRequest(
                        employeeId = session.employeeId,
                        assignmentId = 1L,
                        eventType = eventType,
                        eventTimeDevice = LocalDateTime.now().toString(),
                        geoLat = lat,
                        geoLng = lng,
                        deviceId = "android-demo-01",
                        idempotencyKey = key
                    )
                )

                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    message = "Thành công: ${response.data?.eventType ?: eventType}",
                    queueCount = queueStore.size()
                )

                loadHistory()
            } catch (e: Exception) {
                val session = sessionManager.sessionFlow.first()
                val lat = location?.latitude ?: 10.7769
                val lng = location?.longitude ?: 106.7009

                queueStore.enqueue(
                    QueuedAttendanceAction(
                        employeeId = session.employeeId,
                        assignmentId = 1L,
                        eventType = eventType,
                        eventTimeDevice = LocalDateTime.now().toString(),
                        geoLat = lat,
                        geoLng = lng,
                        deviceId = "android-demo-01",
                        idempotencyKey = key
                    )
                )

                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    message = "Không gửi được, đã đưa vào hàng chờ",
                    queueCount = queueStore.size()
                )
            }
        }
    }
}