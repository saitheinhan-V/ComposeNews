package com.my.composenews.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.my.composenews.data.dispatcher.DispatcherModule
import com.my.composenews.ui.theme.DayNightTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AppPreferenceStoreImpl @Inject constructor(
    private val ds: DataStore<Preferences>,
    @DispatcherModule.IoDispatcher private val io: CoroutineDispatcher = Dispatchers.IO
) : AppPreferenceStore {
    companion object {
        const val APP_PREF_NAME = "com.compose.news"
        val DAY_NIGHT_THEME = stringPreferencesKey("theme.day.night")
        val DYNAMIC_THEME = booleanPreferencesKey("theme.dynamic")
    }

    override suspend fun pullIsDynamic(): Boolean {
        return ds.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences())
                else throw e
            }.map {
                it[DYNAMIC_THEME]
            }.flowOn(io)
            .firstOrNull() ?: false
    }

    override suspend fun putDayNightTheme(theme: DayNightTheme) {
        withContext(io) {
            ds.edit {
                it[DAY_NIGHT_THEME] = theme.name
            }
        }
    }

    override suspend fun pullDayNightTheme(): Flow<DayNightTheme> {
        return ds.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else throw e
            }
            .map {
                it[DAY_NIGHT_THEME]?.let { s ->
                    DayNightTheme.valueOf(s)
                } ?: DayNightTheme.SYSTEM
            }.flowOn(io)
    }
}