package com.hrmapp.mobile.feature.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.ApprovalApi
import com.hrmapp.mobile.core.network.ApprovalStepItem
import com.hrmapp.mobile.core.network.DashboardApi
import com.hrmapp.mobile.core.network.ManagerDashboardData
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagerDashboardViewModel @Inject constructor(
    private val dashboardApi: DashboardApi,
    private val approvalApi: ApprovalApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val data: ManagerDashboardData? = null,
        val pendingApprovals: List<ApprovalStepItem> = emptyList(),
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val session = sessionManager.sessionFlow.first()

                if (!session.roleCode.equals("MANAGER", true)
                    && !session.roleCode.equals("ADMIN", true)
                    && !session.roleCode.equals("HR", true)
                ) {
                    _uiState.value = UiState(
                        isLoading = false,
                        message = "Bạn không có quyền xem bảng điều khiển quản lý"
                    )
                    return@launch
                }

                val dashboardResponse = dashboardApi.getManagerDashboard(session.employeeId)
                val approvalResponse = approvalApi.getPending(session.employeeId)

                val pendingList = approvalResponse.data ?: emptyList()

                _uiState.value = UiState(
                    isLoading = false,
                    data = dashboardResponse.data,
                    pendingApprovals = pendingList,
                    message = if (dashboardResponse.data == null) "Không có dữ liệu dashboard" else ""
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