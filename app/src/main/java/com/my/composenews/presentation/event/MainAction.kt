package com.my.composenews.presentation.event

import com.my.composenews.domain.vo.NewsVo

sealed interface MainAction {

    data class ClickFavour(val item: NewsVo): MainAction

    data class ItemClick(val item: NewsVo): MainAction

    data class SwitchTheme(val isDark: Boolean): MainAction
}