package com.hrmapp.mobile.feature.approval

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.network.ApprovalActionRequest
import com.hrmapp.mobile.core.network.ApprovalApi
import com.hrmapp.mobile.core.network.ApprovalStepItem
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApprovalViewModel @Inject constructor(
    private val approvalApi: ApprovalApi,
    private val sessionManager: SessionManager
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val items: List<ApprovalStepItem> = emptyList(),
        val selectedItem: ApprovalStepItem? = null,
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
                        message = "Bạn không có quyền xem hàng chờ phê duyệt"
                    )
                    return@launch
                }

                val response = approvalApi.getPending(session.employeeId)
                val items = response.data ?: emptyList()

                _uiState.value = UiState(
                    isLoading = false,
                    items = items,
                    selectedItem = items.firstOrNull(),
                    message = if (items.isEmpty()) "Không có yêu cầu chờ duyệt" else ""
                )
            } catch (e: Exception) {
                _uiState.value = UiState(
                    isLoading = false,
                    items = emptyList(),
                    message = e.message ?: "Không tải được hàng chờ duyệt"
                )
            }
        }
    }

    fun select(item: ApprovalStepItem) {
        _uiState.value = _uiState.value?.copy(selectedItem = item)
    }

    fun action(decision: String, note: String) {
        viewModelScope.launch {
            try {
                val item = _uiState.value?.selectedItem ?: return@launch

                approvalApi.action(
                    ApprovalActionRequest(
                        approvalStepId = item.approvalStepId,
                        decision = decision,
                        decisionNote = note
                    )
                )

                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    message = e.message ?: "Không thực hiện được thao tác"
                )
            }
        }
    }
}