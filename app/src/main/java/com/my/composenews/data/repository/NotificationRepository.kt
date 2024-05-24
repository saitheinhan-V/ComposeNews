package com.my.composenews.data.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository{

    suspend fun saveNotificationId(id: Int)

    suspend fun getNotificationId(): Flow<Int>
}