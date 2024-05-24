package com.my.composenews.data.repository

import com.my.composenews.data.dispatcher.DispatcherModule
import com.my.composenews.data.store.AppPreferenceStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepoImpl @Inject constructor(
    private val appPref: AppPreferenceStore,
    @DispatcherModule.IoDispatcher private val io: CoroutineDispatcher
): NotificationRepository {
    override suspend fun saveNotificationId(id: Int) {
        withContext(io){
            appPref.putNotificationId(id)
        }
    }

    override suspend fun getNotificationId(): Flow<Int> {
        return appPref.pullNotificationId().flowOn(io)
    }

}