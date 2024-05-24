package com.my.composenews.presentation.event

import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.composenews.domain.vo.NewsVo
import com.my.composenews.ui.theme.DayNightTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onEmpty

data class MainViewModelState(
    val isLoading: Boolean = false,
    val isError: String = "",
    val news: Flow<PagingData<NewsVo>> = emptyFlow(),
    val page: Int = 0,
    val appTheme: DayNightTheme = DayNightTheme.DAY,
    val notificationId: Int = -1
){
    fun asLoading() = isLoading

    fun asNews() = news

    fun asAppTheme() = appTheme

    fun asNotificationId() = notificationId
}