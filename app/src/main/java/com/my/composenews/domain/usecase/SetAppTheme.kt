package com.my.composenews.domain.usecase

import com.my.composenews.data.store.AppPreferenceStore
import com.my.composenews.ui.theme.DayNightTheme
import javax.inject.Inject

class SetAppTheme @Inject constructor(
    private val appPref: AppPreferenceStore
) {
    suspend operator fun invoke(theme: DayNightTheme){
        appPref.putDayNightTheme(theme)
    }
}