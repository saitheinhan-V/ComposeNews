package com.my.composenews.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.my.composenews.domain.vo.NewsVo
import com.my.composenews.presentation.event.MainEvent
import com.my.composenews.ui.dialog.LoadingDialog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainView(
    news: List<NewsVo> = arrayListOf(),
    event: SharedFlow<MainEvent> = MutableSharedFlow(),
    isLoading: Boolean = false,
) {
    val snackState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                is MainEvent.showSnack -> {
                    snackState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            if (isLoading) {
                LoadingDialog(
                    message = "Loading..."
                )
            }else{
                LazyColumn {
                    itemsIndexed(
                        items = news,
                    ) { index, item ->
                        MainContent(
                            modifier = Modifier.padding(it),
                            item = item
                        )
                    }
                }
            }
        }
    }
}