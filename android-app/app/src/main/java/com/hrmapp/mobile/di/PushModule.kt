package com.hrmapp.mobile.di

import android.content.Context
import com.hrmapp.mobile.core.push.PushManager
import com.hrmapp.mobile.core.push.PushTokenStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PushModule {

    @Provides
    @Singleton
    fun providePushTokenStore(
        @ApplicationContext context: Context
    ): PushTokenStore {
        return PushTokenStore(context)
    }

    @Provides
    @Singleton
    fun providePushManager(
        pushTokenStore: PushTokenStore
    ): PushManager {
        return PushManager(pushTokenStore)
    }
}