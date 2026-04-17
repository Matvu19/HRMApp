package com.hrmapp.mobile.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.EmployeeApi
import com.hrmapp.mobile.core.network.EmployeeProfileData
import com.hrmapp.mobile.core.storage.SessionData
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val employeeApi: EmployeeApi
) : ViewModel() {

    data class HomeUiState(
        val username: String = "",
        val roleCode: String = "",
        val employeeId: Long = 0L,
        val userId: Long = 0L,
        val fullName: String = "",
        val employeeCode: String = "",
        val isManager: Boolean = false,
        val message: String = ""
    )

    private val _uiState = MutableLiveData(HomeUiState())
    val uiState: LiveData<HomeUiState> = _uiState

    fun loadSession() {
        viewModelScope.launch {
            try {
                val session: SessionData = sessionManager.sessionFlow.first()
                val profileResponse = employeeApi.getEmployeeById(session.employeeId)
                val profile: EmployeeProfileData? = profileResponse.data

                _uiState.value = HomeUiState(
                    username = session.username,
                    roleCode = session.roleCode,
                    employeeId = session.employeeId,
                    userId = session.userId,
                    fullName = profile?.fullName ?: session.username,
                    employeeCode = profile?.employeeCode ?: "",
                    isManager = session.roleCode.equals("MANAGER", ignoreCase = true)
                            || session.roleCode.equals("ADMIN", ignoreCase = true)
                            || session.roleCode.equals("HR", ignoreCase = true),
                    message = if (profile == null) "Không tải được hồ sơ nhân sự" else ""
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    message = e.message ?: "Không tải được dữ liệu trang chủ"
                )
            }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onDone()
        }
    }
}