package com.my.composenews.domain.usecase

import com.my.composenews.domain.usecase.GetNews
import javax.inject.Inject

data class MainUseCase @Inject constructor(
    val getNews: GetNews,
    val getAppTheme: GetAppTheme,
    val setAppTheme: SetAppTheme
)