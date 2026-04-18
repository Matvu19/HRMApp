package com.hrmapp.mobile.feature.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.DashboardApi
import com.hrmapp.mobile.core.network.ManagerDashboardData
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ManagerDashboardViewModel @Inject constructor(
    private val dashboardApi: DashboardApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val data: ManagerDashboardData? = null,
        val attendanceRateText: String = "0%",
        val attendanceRateNote: String = "",
        val quickHighlight: String = "",
        val teamHealth: String = "",
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
                        message = "Bạn không có quyền xem dashboard quản lý"
                    )
                    return@launch
                }

                val response = dashboardApi.getManagerDashboard(session.employeeId)
                val data = response.data

                if (data == null) {
                    _uiState.value = UiState(
                        isLoading = false,
                        message = "Không có dữ liệu dashboard"
                    )
                    return@launch
                }

                val attendanceRate = if (data.teamSize > 0) {
                    ((data.todayCheckinCount.toDouble() / data.teamSize.toDouble()) * 100).roundToInt()
                } else {
                    0
                }

                val attendanceRateNote = if (data.teamSize > 0) {
                    "${data.todayCheckinCount}/${data.teamSize} nhân sự đã check-in hôm nay"
                } else {
                    "Chưa có dữ liệu nhân sự trong team"
                }

                val quickHighlight = buildString {
                    append("Hôm nay team có ${data.todayCheckinCount} lượt check-in. ")
                    append("Hiện còn ${data.pendingApprovalCount} yêu cầu chờ duyệt. ")
                    append("Bạn đang có ${data.unreadNotificationCount} thông báo chưa đọc.")
                }

                val teamHealth = when {
                    data.teamSize == 0 -> "Team chưa có dữ liệu nhân sự."
                    attendanceRate >= 80 && data.pendingApprovalCount <= 2 ->
                        "Vận hành ổn định. Tỷ lệ hiện diện tốt và lượng phê duyệt đang ở mức an toàn."
                    attendanceRate < 50 ->
                        "Cần chú ý hiện diện hôm nay. Tỷ lệ check-in còn thấp so với quy mô team."
                    data.pendingApprovalCount >= 5 ->
                        "Khối lượng phê duyệt đang tăng. Nên ưu tiên xử lý hàng chờ để tránh dồn việc."
                    else ->
                        "Team đang vận hành bình thường, nhưng vẫn cần theo dõi thêm các yêu cầu đang chờ."
                }

                _uiState.value = UiState(
                    isLoading = false,
                    data = data,
                    attendanceRateText = "$attendanceRate%",
                    attendanceRateNote = attendanceRateNote,
                    quickHighlight = quickHighlight,
                    teamHealth = teamHealth,
                    message = ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    message = e.message ?: "Không tải được dashboard quản lý"
                )
            }
        }
    }
}