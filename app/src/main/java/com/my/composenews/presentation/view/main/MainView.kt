package com.my.composenews.presentation.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.my.composenews.data.AppConstant
import com.my.composenews.domain.vo.NewsVo
import com.my.composenews.presentation.event.MainAction
import com.my.composenews.presentation.event.MainEvent
import com.my.composenews.presentation.event.NewsUiState
import com.my.composenews.presentation.view.ScrollIndicator
import com.my.composenews.presentation.view.shrimmer.NewsListShimmer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    news: Flow<PagingData<NewsVo>> = emptyFlow(),
    event: SharedFlow<MainEvent> = MutableSharedFlow(),
    onAction: (MainAction) -> Unit = {},
    category: String = AppConstant.CATEGORY
) {
    val snackState: SnackbarHostState = remember { SnackbarHostState() }
    val pagingItems = news.collectAsLazyPagingItems()
    val context = LocalContext.current

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showIndicator = remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.size > 3 && !scrollState.isScrollInProgress
        }
    }

    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                is MainEvent.showSnack -> {
                    snackState.showSnackbar(it.message)
                }
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        pagingItems.apply {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = scrollState
            ) {
                when (loadState.refresh) {
                    is LoadState.Loading -> {
//                            LoadingDialog(
//                                message = "Loading.."
//                            )
                        item {
                            NewsListShimmer()
                        }
                    }

                    is LoadState.Error -> {
                        val e = pagingItems.loadState.refresh as LoadState.Error
                        val msg = e.error.localizedMessage
                    }

                    is LoadState.NotLoading -> {}

                }
                itemsIndexed(
                    items = pagingItems,
                    key = null,
                ){index, item ->
                    MainContent(
                        modifier = modifier,
                        item = item!!,
                        onItemClick = { new ->
                            onAction(MainAction.ItemClick(new))
                        }
                    )
                }
                when(loadState.append){
                    is LoadState.Error -> {
                        val e = pagingItems.loadState.append as LoadState.Error
                        val error = e.error.localizedMessage!!
                        item {
                            Text(
                                text = error
                            )
                        }
                    }
                    LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                    is LoadState.NotLoading -> {}
                }

                if(loadState.append.endOfPaginationReached){
                    if(pagingItems.itemCount == 0){
                        //no data
                        item {
                            Text(
                                text = "No data..."
                            )
                        }
                    }else{
                        //have data but end of page
                        item {
                            Text(
                                text = "End of Page...",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        ScrollIndicator(
            visible = showIndicator.value,
            onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }
        )
    }
}