package com.my.composenews.presentation

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.my.composenews.data.notification.NotificationHandler
import com.my.composenews.data.notification.NotificationParams
import com.my.composenews.domain.vo.MessageVo
import com.my.composenews.presentation.event.MainActivityEvent
import com.my.composenews.presentation.view.MainScreen
import com.my.composenews.ui.theme.AppThemeStatus
import com.my.composenews.ui.theme.ComposeNewsTheme
import com.my.composenews.ui.theme.DayNightTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: MainActivityViewModel by viewModels()

    @Inject
    lateinit var notifier: NotificationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        lifecycleScope.launch {
            observeEvent()
        }

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

    private suspend fun observeEvent() {
        vm.uiEvent.collectLatest {
            when (it) {
                is MainActivityEvent.MessageEvent -> {
                    val id = System.currentTimeMillis().toInt()
                    showNotification(this@MainActivity,id,it.msg)
                }
            }
        }
    }

    fun showNotification(context: Context,id: Int,msg: MessageVo) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> PendingIntent.FLAG_MUTABLE
            else -> {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

        val notification = notifier.getNotificationByMessage(
            params = NotificationParams.Message(
                msg = msg,
                intent = pendingIntent
            )
        )

        with(NotificationManagerCompat.from(context)) {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@with
            }

            Log.i("notification.id","Show $id")
//            notify(id, builder.build())
            notifier.show(notification)
        }
    }

}