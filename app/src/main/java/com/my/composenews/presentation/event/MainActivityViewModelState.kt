package com.my.composenews.presentation.event

import com.my.composenews.ui.theme.DayNightTheme

data class MainActivityViewModelState(
    val appTheme: DayNightTheme = DayNightTheme.DAY
){
    fun asAppTheme() = appTheme
}