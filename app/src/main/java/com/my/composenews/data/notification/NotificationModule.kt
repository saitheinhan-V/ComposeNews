package com.my.composenews.data.notification

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotificationModule {

    @Binds
    @Singleton
    fun bindNotificationHandler(
        notificationHandlerImpl: NotificationHandlerImpl
    ): NotificationHandler
}