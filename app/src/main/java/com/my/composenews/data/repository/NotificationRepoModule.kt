package com.my.composenews.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotificationRepoModule {

    @Binds
    @Singleton
    fun bindNotificationRepo(
        notificationRepoImpl: NotificationRepoImpl
    ): NotificationRepository
}