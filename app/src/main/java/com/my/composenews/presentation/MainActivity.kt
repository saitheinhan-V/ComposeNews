package com.my.composenews.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.my.composenews.presentation.view.main.MainScreen
import com.my.composenews.ui.theme.AppThemeStatus
import com.my.composenews.ui.theme.ComposeNewsTheme
import com.my.composenews.ui.theme.DayNightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val themeState = vm.appTheme.collectAsState()
            val is12AndAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            val isSystemDark = isSystemInDarkTheme()
            val appThemeStatus = if (is12AndAbove) {
                when (themeState.value) {
                    DayNightTheme.DAY -> AppThemeStatus.DynamicLight
                    DayNightTheme.NIGHT -> AppThemeStatus.DynamicDark
                    DayNightTheme.SYSTEM -> if (isSystemDark) AppThemeStatus.DynamicDark else AppThemeStatus.DynamicLight
                }
            } else {
                when (themeState.value) {
                    DayNightTheme.DAY -> AppThemeStatus.Light
                    DayNightTheme.NIGHT -> AppThemeStatus.Dark
                    DayNightTheme.SYSTEM -> if (isSystemDark) AppThemeStatus.Dark else AppThemeStatus.Light
                }
            }
            ComposeNewsTheme(
                appThemeStatus = appThemeStatus,
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}