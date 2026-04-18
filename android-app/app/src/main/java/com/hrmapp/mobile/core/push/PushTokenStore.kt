package com.hrmapp.mobile.core.push

import android.content.Context

class PushTokenStore(
    context: Context
) {
    private val prefs = context.getSharedPreferences("push_prefs", Context.MODE_PRIVATE)

    fun getToken(): String {
        return prefs.getString("fcm_token", "") ?: ""
    }

    fun saveToken(token: String) {
        prefs.edit().putString("fcm_token", token).apply()
    }
}