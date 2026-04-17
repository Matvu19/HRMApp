package com.hrmapp.mobile.feature.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.ApprovalActionRequest
import com.hrmapp.mobile.core.network.ApprovalApi
import com.hrmapp.mobile.core.network.ApprovalStepItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApprovalViewModel @Inject constructor(
    private val approvalApi: ApprovalApi
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val item: ApprovalStepItem? = null,
        val message: String = ""
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun load(approverEmployeeId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)

            try {
                val response = approvalApi.getPending(approverEmployeeId)
                val first = response.data?.firstOrNull()
                _uiState.value = UiState(
                    isLoading = false,
                    item = first,
                    message = if (first == null) "Không có yêu cầu chờ duyệt" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    message = e.message ?: "Không tải được hàng chờ duyệt"
                )
            }
        }
    }

    fun action(stepId: Long, decision: String, note: String) {
        viewModelScope.launch {
            try {
                val response = approvalApi.action(
                    ApprovalActionRequest(
                        approvalStepId = stepId,
                        decision = decision,
                        decisionNote = note
                    )
                )
                _uiState.value = _uiState.value?.copy(
                    item = response.data,
                    message = if (decision == "APPROVED") "Đã duyệt yêu cầu" else "Đã từ chối yêu cầu"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    message = e.message ?: "Không thực hiện được thao tác"
                )
            }
        }
    }
}