package com.hrmapp.mobile.core.util

import android.content.Context
import android.util.Log
import java.lang.Thread.UncaughtExceptionHandler

class AppCrashHandler(
    private val context: Context,
    private val defaultHandler: UncaughtExceptionHandler?
) : UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Log.e("HRMAppCrash", "Uncaught exception on thread=${thread.name}", throwable)

        try {
            val prefs = context.getSharedPreferences("crash_log", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("last_crash_message", throwable.message ?: "Unknown crash")
                .putLong("last_crash_time", System.currentTimeMillis())
                .apply()
        } catch (_: Exception) {
        }

        defaultHandler?.uncaughtException(thread, throwable)
    }
}