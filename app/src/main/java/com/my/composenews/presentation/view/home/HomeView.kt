package com.my.composenews.presentation.view.home

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.my.composenews.R
import com.my.composenews.data.AppConstant
import com.my.composenews.domain.vo.NewsVo
import com.my.composenews.presentation.MainActivity
import com.my.composenews.presentation.event.MainAction
import com.my.composenews.presentation.event.MainEvent
import com.my.composenews.presentation.utils.DateConverterUtils
import com.my.composenews.presentation.view.ScrollIndicator
import com.my.composenews.presentation.view.shrimmer.NewsListShimmer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    news: Flow<PagingData<NewsVo>> = emptyFlow(),
    event: SharedFlow<MainEvent> = MutableSharedFlow(),
    onAction: (MainAction) -> Unit = {},
    category: String = AppConstant.CATEGORY,
    saveNotification: (Int) -> Unit = {},
    currentNotificationId: Int = 0
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

    val notificationPermission =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(key1 = Unit) {
        event.collectLatest {
            when (it) {
                is MainEvent.showSnack -> {
                    snackState.showSnackbar(it.message)
                }
            }
        }
    }

    val currentDateTime = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
    val currentTimeMillis = System.currentTimeMillis()
    val inputTimeMillis = 1717113030000
    val inputDateTime = Instant.fromEpochMilliseconds(1717113030000).toLocalDateTime(TimeZone.currentSystemDefault())
    val dayOfYear = remember { inputDateTime.dayOfYear }
    val week = remember {
        currentDateTime.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("EEE"))
    }
    val month = remember {
        inputDateTime.month
    }
    val year = remember {
        inputDateTime.year
    }
    val diffWeekDay = remember {
        currentDateTime.dayOfYear-inputDateTime.dayOfYear
    }
    val dayOfWeek = remember {
        inputDateTime.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern("EEE"))
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
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Text(text = "Day of Year: $dayOfYear $week")
                            Text(text = "Month: $month, Year: $year")
                            Text(text = "Diff: $diffWeekDay $dayOfWeek")
                            Text(text = DateConverterUtils.formatDateTime(currentTimeMillis,inputTimeMillis,false,"yesterday","today"))
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    if (!notificationPermission.status.isGranted)
                                        notificationPermission.launchPermissionRequest()
                                    else {
                                        val id = System.currentTimeMillis().toInt()
                                        saveNotification(id)
                                        showNotification(context = context, id = id)
                                    }
                                }
                            }
                        ) {
                            Text("Show Notification")
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    cancelNotification(context, currentNotificationId)
                                }
                            }
                        ) {
                            Text("Hide Notification")
                        }
                    }
                }
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
                ) { index, item ->
                    MainContent(
                        modifier = modifier,
                        item = item!!,
                        onItemClick = { new ->
                            onAction(MainAction.ItemClick(new))
                        }
                    )
                }
                when (loadState.append) {
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

                if (loadState.append.endOfPaginationReached) {
                    if (pagingItems.itemCount == 0) {
                        //no data
                        item {
                            Text(
                                text = "No data..."
                            )
                        }
                    } else {
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

fun doesNotificationChannelExist(context: Context, channelId: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existingChannel = notificationManager.getNotificationChannel(channelId)
        return existingChannel != null
    }
    return false
}

fun showNotification(context: Context, id: Int) {
    //create channel
//    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
//
//        if(!doesNotificationChannelExist(context,"100")){
//            val name = "100"
//            val descriptionText = ""
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel("100",name,importance).apply {
//                description = descriptionText
//            }
//            val notificationManager: NotificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

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

    val builder = NotificationCompat.Builder(context, "100")
        .setSmallIcon(R.drawable.ic_small_notification)
        .setContentTitle("Compose News Notification")
        .setContentText(id.toString())
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setGroup("MjU5NDAwNDAwMTcxNjI2ODEyMDUwMQ")
//        .setStyle(NotificationCompat.InboxStyle()
//            .addLine("Message 1")
//            .addLine("Message 2")
//            .setBigContentTitle("2 new messages")
//            .setSummaryText("user@example.com"))
//        .setGroupSummary(true)
        .setAutoCancel(true)

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

        Log.i("notification.id", "Show $id")
        notify(id, builder.build())
    }
}

fun cancelNotification(context: Context, id: Int) {
    Log.i("notification.id", "Hide $id")

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//    notificationManager.cancel(id)
    val activeNotifications = notificationManager.activeNotifications
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        activeNotifications.forEach {
            if (it.notification.group == "MjU5NDAwNDAwMTcxNjI2ODEyMDUwMQ") {
                notificationManager.cancel(it.id)
            }
        }
    }

}

fun getDayOfYear(localDateTime: LocalDateTime): Int {
    return localDateTime.dayOfYear
}
