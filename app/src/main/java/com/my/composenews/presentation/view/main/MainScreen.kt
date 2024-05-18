package com.my.composenews.presentation.view.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.composenews.R
import com.my.composenews.presentation.MainViewModel
import com.my.composenews.presentation.event.MainAction
import com.my.composenews.presentation.event.MainViewModelState
import com.my.composenews.ui.theme.ComposeNewsTheme
import com.my.composenews.ui.theme.DayNightTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val news = viewModel.newsList.collectAsState()
    val appTheme = viewModel.appTheme.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isDarkTheme = remember {
        derivedStateOf {
            when(appTheme.value){
                DayNightTheme.DAY -> false
                DayNightTheme.NIGHT -> true
                DayNightTheme.SYSTEM -> false
            }
        }
    }

    ModalNavigationDrawer(
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
                },
                content = {
                    MainView(
                        modifier = Modifier.padding(it),
                        news = news.value,
                        event = viewModel.uiEvent,
                        onAction = viewModel::onActionMain
                    )
                }
            )

        }
    )

}