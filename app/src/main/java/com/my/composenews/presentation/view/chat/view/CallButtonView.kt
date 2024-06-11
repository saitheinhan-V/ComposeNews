package com.my.composenews.presentation.view.chat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.my.composenews.R

@Composable
fun CallButtonView(
    callEnd: () -> Unit = {},
    onFlip: (Boolean) -> Unit = {},
    onCamera: (Boolean) -> Unit = {},
    onMute: (Boolean) -> Unit = {},
    flip: Boolean = false,
    camera: Boolean = false,
    mute: Boolean = false
){
    Column (
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                onClick = {
                    onFlip(!flip)
                }
            ){
                Icon(
                    painter = painterResource(id = R.drawable.ic_flip_camera),
                    contentDescription = null
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ),
                onClick = {
                    onCamera(camera)
                }
            ){
                Icon(
                    painter = if(camera) painterResource(id = R.drawable.ic_video_on)
                    else painterResource(id = R.drawable.ic_video_off),
                    contentDescription = null
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                onClick = {
                    onMute(!mute)
                }
            ){
                Icon(
                    painter = if(mute) painterResource(id = R.drawable.ic_mic_off)
                    else painterResource(id = R.drawable.ic_mic_on),
                    contentDescription = null
                )
            }
        }
        Button(
            modifier = Modifier.padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            onClick = {
                callEnd()
            }
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_call_end),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewCallButton(){
    Surface {
        CallButtonView()
    }
}