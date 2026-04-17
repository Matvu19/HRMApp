package com.hrmapp.mobile.feature.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.DashboardApi
import com.hrmapp.mobile.core.network.ManagerDashboardData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagerDashboardViewModel @Inject constructor(
    private val dashboardApi: DashboardApi
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val data: ManagerDashboardData? = null,
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load(managerEmployeeId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val response = dashboardApi.getManagerDashboard(managerEmployeeId)
                _uiState.value = UiState(
                    isLoading = false,
                    data = response.data,
                    message = if (response.data == null) "Không có dữ liệu" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    message = e.message ?: "Không tải được bảng điều khiển"
                )
            }
        }
    }
}