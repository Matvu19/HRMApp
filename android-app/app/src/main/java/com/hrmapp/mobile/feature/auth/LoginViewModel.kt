package com.hrmapp.mobile.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.AuthApi
import com.hrmapp.mobile.core.network.LoginRequest
import com.hrmapp.mobile.core.storage.SavedAccount
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val username: String = "",
        val roleCode: String = "",
        val message: String = "",
        val savedAccounts: List<SavedAccount> = emptyList()
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    init {
        loadSavedAccounts()
    }

    fun loadSavedAccounts() {
        viewModelScope.launch {
            sessionManager.savedAccountsFlow.collect { accounts ->
                _uiState.postValue(
                    _uiState.value?.copy(savedAccounts = accounts) ?: UiState(savedAccounts = accounts)
                )
            }
        }
    }

    fun removeSavedAccount(username: String) {
        viewModelScope.launch {
            sessionManager.removeSavedAccount(username)
        }
    }

    fun login(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(
                isLoading = true,
                isSuccess = false,
                message = ""
            )

            try {
                val response = authApi.login(
                    LoginRequest(
                        username = username,
                        password = password
                    )
                )

                val data = response.data
                if (data == null) {
                    _uiState.value = _uiState.value?.copy(
                        isLoading = false,
                        isSuccess = false,
                        message = response.message ?: "Đăng nhập thất bại"
                    )
                    return@launch
                }

                sessionManager.saveSession(
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken,
                    username = data.username,
                    roleCode = data.roleCode,
                    userId = data.userId,
                    employeeId = data.employeeId
                )

                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    isSuccess = true,
                    username = data.username,
                    roleCode = data.roleCode,
                    message = response.message ?: "Đăng nhập thành công"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    isSuccess = false,
                    message = e.message ?: "Không kết nối được máy chủ"
                )
            }
        }
    }
}