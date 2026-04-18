package com.hrmapp.mobile

import android.app.Application
import com.hrmapp.mobile.core.push.PushManager
import com.hrmapp.mobile.core.util.AppCrashHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HRMAppApplication : Application() {

    @Inject
    lateinit var pushManager: PushManager

    override fun onCreate() {
        super.onCreate()

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            AppCrashHandler(
                context = applicationContext,
                defaultHandler = defaultHandler
            )
        )

        pushManager.fetchToken()
        pushManager.subscribeGeneralTopic()
    }
}