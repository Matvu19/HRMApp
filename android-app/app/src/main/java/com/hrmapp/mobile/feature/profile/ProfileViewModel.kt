package com.hrmapp.mobile.feature.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.AccountApi
import com.hrmapp.mobile.core.network.ChangePasswordRequest
import com.hrmapp.mobile.core.network.EmployeeApi
import com.hrmapp.mobile.core.network.EmployeeProfileData
import com.hrmapp.mobile.core.network.UpdateEmployeeProfileRequest
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val employeeApi: EmployeeApi,
    private val accountApi: AccountApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val profile: EmployeeProfileData? = null,
        val username: String = "",
        val roleCode: String = "",
        val isEditing: Boolean = false,
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load() {
        viewModelScope.launch {
            val oldState = _uiState.value ?: UiState()
            _uiState.value = oldState.copy(isLoading = true, message = "")

            try {
                val session = sessionManager.sessionFlow.first()
                val response = employeeApi.getEmployeeById(session.employeeId)

                _uiState.value = UiState(
                    isLoading = false,
                    profile = response.data,
                    username = session.username,
                    roleCode = session.roleCode,
                    isEditing = false,
                    message = response.message ?: ""
                )
            } catch (e: Exception) {
                val session = sessionManager.sessionFlow.first()
                _uiState.value = UiState(
                    isLoading = false,
                    profile = null,
                    username = session.username,
                    roleCode = session.roleCode,
                    isEditing = false,
                    message = e.message ?: "Không tải được hồ sơ"
                )
            }
        }
    }

    fun setEditing(editing: Boolean) {
        _uiState.value = _uiState.value?.copy(
            isEditing = editing,
            message = if (editing) "Bạn đang ở chế độ chỉnh sửa" else ""
        )
    }

    fun updateProfile(
        fullName: String,
        personalPhone: String,
        gender: String
    ) {
        viewModelScope.launch {
            val state = _uiState.value ?: return@launch
            val profile = state.profile ?: return@launch

            _uiState.value = state.copy(isLoading = true, message = "")

            try {
                val response = employeeApi.updateEmployeeById(
                    profile.employeeId,
                    UpdateEmployeeProfileRequest(
                        fullName = fullName.ifBlank { profile.fullName },
                        personalPhone = personalPhone.ifBlank { null },
                        gender = gender.ifBlank { null }
                    )
                )

                _uiState.value = state.copy(
                    isLoading = false,
                    profile = response.data ?: profile,
                    isEditing = false,
                    message = response.message ?: "Đã cập nhật thông tin cá nhân"
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoading = false,
                    isEditing = true,
                    message = e.message ?: "Không cập nhật được thông tin"
                )
            }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onDone: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = accountApi.changePassword(
                    ChangePasswordRequest(
                        currentPassword = currentPassword,
                        newPassword = newPassword
                    )
                )

                onDone(
                    response.success,
                    response.message ?: if (response.success) {
                        "Đổi mật khẩu thành công"
                    } else {
                        "Đổi mật khẩu thất bại"
                    }
                )
            } catch (e: Exception) {
                onDone(false, e.message ?: "Không đổi được mật khẩu")
            }
        }
    }
}