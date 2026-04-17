package com.hrmapp.mobile.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashGateViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    data class GateState(
        val checked: Boolean = false,
        val loggedIn: Boolean = false
    )

    private val _gateState = MutableLiveData(GateState())
    val gateState: LiveData<GateState> = _gateState

    fun checkSession() {
        viewModelScope.launch {
            val session = sessionManager.sessionFlow.first()
            _gateState.value = GateState(
                checked = true,
                loggedIn = session.accessToken.isNotBlank()
            )
        }
    }
}