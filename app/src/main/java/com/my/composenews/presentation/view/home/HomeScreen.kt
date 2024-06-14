package com.my.composenews.presentation.view.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.composenews.R
import com.my.composenews.presentation.MainViewModel
import com.my.composenews.presentation.event.MainAction
import com.my.composenews.ui.theme.DayNightTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val news = viewModel.newsList.collectAsState()
    val appTheme = viewModel.appTheme.collectAsState()
    val currentNotificationId = viewModel.notificationId.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val tabTitles = context.resources.getStringArray(R.array.news_category)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = {
            tabTitles.size
        }
    )


    val isDarkTheme = remember {
        derivedStateOf {
            when (appTheme.value) {
                DayNightTheme.DAY -> false
                DayNightTheme.NIGHT -> true
                DayNightTheme.SYSTEM -> false
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collectLatest { page ->
            viewModel.observeNews(tabTitles[page])
        }
    }

    ModalNavigationDrawer(
        modifier = Modifier.clip(RoundedCornerShape(0.dp)),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    repeat(3) {
                        Text(
                            text = "Item $it",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary
                            )
                            .wrapContentSize()
                    ) {
                        TopAppBar(
                            title = { Text("Compose News") },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    })
                                {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_drawer),
                                        contentDescription = ""
                                    )
                                }
                            },
                            actions = {
                                Switch(
                                    checked = isDarkTheme.value,
                                    onCheckedChange = {
                                        scope.launch {
                                            viewModel.onActionMain(MainAction.SwitchTheme(it))
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                        checkedIconColor = MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            }
                        )
                        ScrollableTabRow(
                            selectedTabIndex = pagerState.currentPage,
                            edgePadding = 0.dp,
//                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            indicator = { tabPosition ->
                                TabRowDefaults.Indicator(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.tabIndicatorOffset(tabPosition[pagerState.currentPage])
                                )
                            }
                        ) {
                            tabTitles.onEachIndexed { index, title ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        scope.launch {
                                            pagerState.scrollToPage(index)
                                            viewModel.observeNews(title)
                                        }
                                    },
                                    text = {
                                        Text(
                                            text = title,
                                        )
                                    },
                                    selectedContentColor = MaterialTheme.colorScheme.primary,
                                    unselectedContentColor = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                },
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxSize()
                        .fillMaxHeight(),
                    pageSize = PageSize.Fill
                ) { page ->
                    HomeView(
                        modifier = Modifier.padding(it),
                        news = news.value,
                        event = viewModel.uiEvent,
                        onAction = viewModel::onActionMain,
                        category = tabTitles[page].lowercase(),
                        saveNotification = {
                            viewModel.keepNotifyId(it)
                        },
                        currentNotificationId = currentNotificationId.value
                    )
                }
            }
        }
    )

}