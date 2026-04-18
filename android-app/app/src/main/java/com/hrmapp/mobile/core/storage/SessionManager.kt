package com.hrmapp.mobile.core.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "hrmapp_session")

class SessionManager(private val context: Context) {

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USERNAME = stringPreferencesKey("username")
        private val ROLE_CODE = stringPreferencesKey("role_code")
        private val USER_ID = longPreferencesKey("user_id")
        private val EMPLOYEE_ID = longPreferencesKey("employee_id")
    }

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        username: String,
        roleCode: String,
        userId: Long,
        employeeId: Long
    ) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[USERNAME] = username
            prefs[ROLE_CODE] = roleCode
            prefs[USER_ID] = userId
            prefs[EMPLOYEE_ID] = employeeId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    val accessTokenFlow: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN] ?: ""
        }

    val usernameFlow: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[USERNAME] ?: ""
        }

    val roleCodeFlow: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[ROLE_CODE] ?: ""
        }

    val userIdFlow: Flow<Long> =
        context.dataStore.data.map { prefs ->
            prefs[USER_ID] ?: 0L
        }

    val employeeIdFlow: Flow<Long> =
        context.dataStore.data.map { prefs ->
            prefs[EMPLOYEE_ID] ?: 0L
        }

    val sessionFlow: Flow<SessionData> =
        context.dataStore.data.map { prefs ->
            SessionData(
                accessToken = prefs[ACCESS_TOKEN] ?: "",
                refreshToken = prefs[REFRESH_TOKEN] ?: "",
                username = prefs[USERNAME] ?: "",
                roleCode = prefs[ROLE_CODE] ?: "",
                userId = prefs[USER_ID] ?: 0L,
                employeeId = prefs[EMPLOYEE_ID] ?: 0L
            )
        }
}

data class SessionData(
    val accessToken: String,
    val refreshToken: String,
    val username: String,
    val roleCode: String,
    val userId: Long,
    val employeeId: Long
)