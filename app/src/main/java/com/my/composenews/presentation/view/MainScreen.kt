package com.my.composenews.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.composenews.presentation.MainViewModel

@Composable
fun MainScreen(){
    val viewModel: MainViewModel = hiltViewModel()
    val news = viewModel.newsList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    MainView(
        news = news.value,
        event = viewModel.uiEvent,
        isLoading = isLoading.value
    )

}