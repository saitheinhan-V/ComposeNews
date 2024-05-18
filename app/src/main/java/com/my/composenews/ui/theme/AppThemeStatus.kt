package com.my.composenews.ui.theme

sealed interface AppThemeStatus {

    data object DynamicLight: AppThemeStatus

    data object DynamicDark: AppThemeStatus

    data object Dark: AppThemeStatus

    data object Light: AppThemeStatus
}

enum class DayNightTheme{
    DAY, NIGHT, SYSTEM
}