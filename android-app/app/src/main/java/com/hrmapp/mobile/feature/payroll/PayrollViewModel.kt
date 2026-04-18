package com.hrmapp.mobile.feature.payroll

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.PayrollApi
import com.hrmapp.mobile.core.network.PayrollItem
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayrollViewModel @Inject constructor(
    private val payrollApi: PayrollApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val items: List<PayrollItem> = emptyList(),
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val session = sessionManager.sessionFlow.first()
                val response = payrollApi.getPayrollsByEmployee(session.employeeId)
                val items = response.data ?: emptyList()

                _uiState.value = UiState(
                    isLoading = false,
                    items = items,
                    message = if (items.isEmpty()) "Chưa có dữ liệu bảng lương" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    items = emptyList(),
                    message = e.message ?: "Không tải được bảng lương"
                )
            }
        }
    }
}