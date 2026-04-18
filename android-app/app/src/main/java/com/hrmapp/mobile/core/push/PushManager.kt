package com.hrmapp.mobile.core.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class PushManager(
    private val pushTokenStore: PushTokenStore
) {

    fun fetchToken(onDone: (String) -> Unit = {}) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                pushTokenStore.saveToken(token)
                onDone(token)
            }
            .addOnFailureListener {
                Log.e("PushManager", "Cannot fetch FCM token", it)
                onDone("")
            }
    }

    fun getSavedToken(): String = pushTokenStore.getToken()

    fun subscribeGeneralTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("general")
    }
}