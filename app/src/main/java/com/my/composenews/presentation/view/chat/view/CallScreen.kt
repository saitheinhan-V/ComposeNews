package com.my.composenews.presentation.view.chat.view

import android.Manifest
import android.util.Log
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.my.composenews.presentation.view.chat.ChatViewModel
import com.my.composenews.presentation.view.chat.state.ActionCall
import kotlin.math.roundToInt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CallScreen(
    vm: ChatViewModel
) {
    val localView = vm.localView.collectAsState()
    val remoteView = vm.remoteView.collectAsState()
    val shouldShowRemote = vm.showRemote.collectAsState()
    val shouldShowLocal = vm.showLocal.collectAsState()
    val mute = vm.isMute.collectAsState()
    val flip = vm.isFlip.collectAsState()
    val speaker = vm.isSpeaker.collectAsState()
    val camera = vm.isCamera.collectAsState()

    val context = LocalContext.current
    val audioPermission = Manifest.permission.RECORD_AUDIO
    val cameraPermission = Manifest.permission.CAMERA
    val callPermission = listOf(audioPermission, cameraPermission)
    val requirePermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            var isGrantedAll by mutableStateOf(false)
            callPermission.forEach { permission ->
                isGrantedAll = perms[permission] == true
            }
            if (isGrantedAll) {
                Log.d("agora.permission", "$isGrantedAll")
            } else {
                Log.d("agora.permission", "false")
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        if (!requirePermissions.allPermissionsGranted) {
            permissionLauncher.launch(arrayOf(cameraPermission, audioPermission))
        }
    }

    Scaffold(

    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {

            if (shouldShowRemote.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    remoteView.let { view ->
                        AndroidView(
                            factory = {
                                view.value!!.second.also { surfaceView ->
                                    surfaceView.apply {
                                        layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
//                    Column(
//                        modifier = Modifier.padding(6.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(text = name)
//                        Text(text = TimeFormatter.convertOnProgressTime(onProgressSecond))
//                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                //local view
                if (shouldShowLocal.value) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .align(Alignment.End)
                    ) {
                        var offsetX by remember { mutableFloatStateOf(0f) }
                        var offsetY by remember { mutableFloatStateOf(0f) }

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val x = dragAmount.x
                                        val y = dragAmount.y

                                        offsetX += x
                                        offsetY += y
                                    }
                                }
                                .padding(10.dp)
                                .background(Color.Green)
                                .shadow(
                                    elevation = 5.dp,
                                    clip = true,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            localView.let { view ->
                                AndroidView(
                                    factory = {
                                        view.value!!.second.also { surfaceView ->
                                            surfaceView.apply {
                                                layoutParams = ViewGroup.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.MATCH_PARENT
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .size(
                                            width = 100.dp,
                                            height = 160.dp
                                        )
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    //button
                    CallButtonView(
                        onFlip = { isFlip ->
                            vm.onActionCall(ActionCall.ClickFlip(isFlip))
                        },
                        onCamera = { isCamera ->
                            vm.onActionCall(ActionCall.ClickCamera(isCamera))
                        },
                        onMute = { isMute ->
                            vm.onActionCall(ActionCall.ClickMute(isMute))
                        },
                        callEnd = {
                            vm.onActionCall(ActionCall.ClickCallEnd)
                        },
                        flip = flip.value,
                        camera = camera.value,
                        mute = mute.value
                    )
                }
            }
        }
    }
}