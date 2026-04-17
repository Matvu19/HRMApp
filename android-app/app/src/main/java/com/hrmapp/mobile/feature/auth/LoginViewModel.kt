package com.hrmapp.mobile.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.AuthApi
import com.hrmapp.mobile.core.network.LoginRequest
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class LoginUiState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = "",
        val username: String = "",
        val roleCode: String = ""
    )

    private val _uiState = MutableLiveData(LoginUiState())
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(
                message = "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            try {
                val response = authApi.login(
                    LoginRequest(
                        username = username.trim(),
                        password = password.trim()
                    )
                )

                if (response.success && response.data != null) {
                    sessionManager.saveSession(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken,
                        username = response.data.username,
                        roleCode = response.data.roleCode,
                        userId = response.data.userId,
                        employeeId = response.data.employeeId
                    )

                    _uiState.value = LoginUiState(
                        isLoading = false,
                        isSuccess = true,
                        message = "Đăng nhập thành công",
                        username = response.data.username,
                        roleCode = response.data.roleCode
                    )
                } else {
                    _uiState.value = LoginUiState(
                        isLoading = false,
                        message = response.message.ifBlank { "Đăng nhập thất bại" }
                    )
                }
            } catch (e: IOException) {
                _uiState.value = LoginUiState(
                    isLoading = false,
                    message = "Không kết nối được tới máy chủ. Hãy kiểm tra backend đang chạy."
                )
            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    isLoading = false,
                    message = e.message ?: "Có lỗi xảy ra khi đăng nhập"
                )
            }
        }
    }
}