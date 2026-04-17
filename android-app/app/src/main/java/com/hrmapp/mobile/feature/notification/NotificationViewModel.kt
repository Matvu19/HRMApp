package com.hrmapp.mobile.feature.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.NotificationApi
import com.hrmapp.mobile.core.network.NotificationItem
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationApi: NotificationApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val items: List<NotificationItem> = emptyList(),
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val session = sessionManager.sessionFlow.first()
                val response = notificationApi.getNotifications(session.userId)
                val items = response.data ?: emptyList()

                _uiState.value = UiState(
                    isLoading = false,
                    items = items,
                    message = if (items.isEmpty()) "Không có thông báo nào" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    items = emptyList(),
                    message = e.message ?: "Không tải được thông báo"
                )
            }
        }
    }

    fun markRead(notificationId: Long) {
        viewModelScope.launch {
            try {
                notificationApi.markRead(notificationId)
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    message = e.message ?: "Không thể cập nhật trạng thái"
                )
            }
        }
    }
}