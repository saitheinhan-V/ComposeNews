package com.my.composenews.data.store

import com.my.composenews.ui.theme.DayNightTheme
import kotlinx.coroutines.flow.Flow

interface AppPreferenceStore {

    suspend fun pullIsDynamic(): Boolean

    suspend fun putDayNightTheme(theme: DayNightTheme)

    suspend fun pullDayNightTheme(): Flow<DayNightTheme>

    suspend fun putNotificationId(id: Int)

    suspend fun pullNotificationId(): Flow<Int>
}