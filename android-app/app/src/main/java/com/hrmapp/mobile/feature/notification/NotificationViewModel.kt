package com.hrmapp.mobile.feature.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.NotificationApi
import com.hrmapp.mobile.core.network.NotificationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationApi: NotificationApi
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val item: NotificationItem? = null,
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load(userId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val response = notificationApi.getNotifications(userId)
                val first = response.data?.firstOrNull()

                _uiState.value = UiState(
                    isLoading = false,
                    item = first,
                    message = if (first == null) "Không có thông báo nào" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    message = e.message ?: "Không tải được thông báo"
                )
            }
        }
    }

    fun markRead(notificationId: Long) {
        viewModelScope.launch {
            try {
                val response = notificationApi.markRead(notificationId)
                _uiState.value = _uiState.value?.copy(
                    item = response.data,
                    message = "Đã đánh dấu đã đọc"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    message = e.message ?: "Không thể cập nhật trạng thái"
                )
            }
        }
    }
}