package com.hrmapp.mobile.feature.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.EmployeeApi
import com.hrmapp.mobile.core.network.EmployeeProfileData
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val employeeApi: EmployeeApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val profile: EmployeeProfileData? = null,
        val username: String = "",
        val roleCode: String = "",
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val session = sessionManager.sessionFlow.first()
                val response = employeeApi.getEmployeeById(session.employeeId)

                _uiState.value = UiState(
                    isLoading = false,
                    profile = response.data,
                    username = session.username,
                    roleCode = session.roleCode,
                    message = if (response.data == null) "Không tải được hồ sơ" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    message = e.message ?: "Không tải được hồ sơ"
                )
            }
        }
    }
}