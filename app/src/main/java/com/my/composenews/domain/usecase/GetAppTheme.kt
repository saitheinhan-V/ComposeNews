package com.my.composenews.domain.usecase

import com.my.composenews.data.dispatcher.DispatcherModule
import com.my.composenews.data.store.AppPreferenceStore
import com.my.composenews.ui.theme.DayNightTheme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAppTheme @Inject constructor(
    private val appPref: AppPreferenceStore,
    @DispatcherModule.IoDispatcher private val io: CoroutineDispatcher = Dispatchers.IO
){
    suspend operator fun invoke(): Flow<DayNightTheme> {
        return appPref.pullDayNightTheme().flowOn(io)
    }
}