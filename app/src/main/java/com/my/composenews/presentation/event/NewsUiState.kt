package com.my.composenews.presentation.event

import androidx.paging.PagingData
import com.my.composenews.domain.vo.NewsVo
import kotlinx.coroutines.flow.Flow

sealed interface NewsUiState {

    data object Loading: NewsUiState

    data class Error(val message: String): NewsUiState

    data class Data(val list: Flow<PagingData<NewsVo>>): NewsUiState
}