package com.my.composenews.presentation.event

import com.my.composenews.domain.vo.NewsVo

data class MainViewModelState(
    val isLoading: Boolean = false,
    val news: List<NewsVo> = mutableListOf(),
    val page: Int = 0
){
    fun asLoading() = isLoading

    fun asNews() = news
}