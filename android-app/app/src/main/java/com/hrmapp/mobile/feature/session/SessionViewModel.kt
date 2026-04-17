package com.hrmapp.mobile.feature.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrmapp.mobile.core.storage.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    var isLoggedIn: Boolean = false
    var username: String = ""
    var role: String = ""

    fun loadSession(onDone: () -> Unit) {
        viewModelScope.launch {
            val session = sessionManager.sessionFlow.first()

            isLoggedIn = session.accessToken.isNotBlank()
            username = session.username
            role = session.roleCode

            onDone()
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onDone()
        }
    }
}