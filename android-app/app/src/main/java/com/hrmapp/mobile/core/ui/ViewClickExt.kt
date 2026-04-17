package com.hrmapp.mobile.core.ui

import android.os.SystemClock
import android.view.View

fun View.setSafeClickListener(
    intervalMs: Long = 800L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime >= intervalMs) {
            lastClickTime = now
            onClick(view)
        }
    }
}