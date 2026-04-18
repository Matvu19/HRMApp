package com.hrmapp.mobile.core.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        private val SAVED_ACCOUNTS = stringPreferencesKey("saved_accounts_json")
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

            val currentAccounts = parseSavedAccounts(prefs[SAVED_ACCOUNTS])
                .filterNot { it.username.equals(username, ignoreCase = true) }
                .toMutableList()

            currentAccounts.add(
                0,
                SavedAccount(
                    username = username,
                    roleCode = roleCode,
                    employeeId = employeeId,
                    userId = userId
                )
            )

            prefs[SAVED_ACCOUNTS] = Gson().toJson(currentAccounts.take(5))
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
            prefs.remove(USERNAME)
            prefs.remove(ROLE_CODE)
            prefs.remove(USER_ID)
            prefs.remove(EMPLOYEE_ID)
        }
    }

    suspend fun removeSavedAccount(username: String) {
        context.dataStore.edit { prefs ->
            val updated = parseSavedAccounts(prefs[SAVED_ACCOUNTS])
                .filterNot { it.username.equals(username, ignoreCase = true) }
            prefs[SAVED_ACCOUNTS] = Gson().toJson(updated)
        }
    }

    suspend fun clearSavedAccounts() {
        context.dataStore.edit { prefs ->
            prefs[SAVED_ACCOUNTS] = "[]"
        }
    }

    val savedAccountsFlow: Flow<List<SavedAccount>> =
        context.dataStore.data.map { prefs ->
            parseSavedAccounts(prefs[SAVED_ACCOUNTS])
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

    private fun parseSavedAccounts(raw: String?): List<SavedAccount> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            val type = object : TypeToken<List<SavedAccount>>() {}.type
            Gson().fromJson(raw, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
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