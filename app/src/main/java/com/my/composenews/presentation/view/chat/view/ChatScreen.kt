package com.my.composenews.presentation.view.chat.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.my.composenews.R
import com.my.composenews.data.AppConstant
import com.my.composenews.presentation.view.chat.ChatViewModel
import com.my.composenews.presentation.view.chat.state.ActionChat
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    vm: ChatViewModel
) {
//    val vm: ChatViewModel = hiltViewModel()
    val currentChannel = vm.currentChannel.collectAsState()
    val scope = rememberCoroutineScope()
    val channelError = vm.channelError.collectAsState()

    val isCloseIconVisible = currentChannel.value.isNotEmpty()
    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            value = currentChannel.value,
            onValueChange = { value ->
                vm.onAction(ActionChat.ChangeUser(value.trim()))
            },
            placeholder = {
                Text(text = "Enter channel id")
            },
            isError = channelError.value.isError,
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = Color.Red,
                focusedBorderColor = if (channelError.value.isError) Color.Red else Color.Blue,
                unfocusedBorderColor = if (channelError.value.isError) Color.Red else Color.Gray,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            shape = CircleShape,
            trailingIcon = {
                IconButton(
                    onClick = {
                        vm.onAction(ActionChat.ClearUser)
                    }
                ) {
                    if (isCloseIconVisible) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = "Clear Text",
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    }
                }
            }
        )
        AnimatedVisibility(
            visible = channelError.value.isError
        ) {
            Text(
                text = channelError.value.errMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 15.dp),
            )
        }

        Button(
            onClick = {
                scope.launch {
                    val uid = AppConstant.UID.toInt()
                    vm.onAction(ActionChat.ClickJoin(uid))
                }
            },
        ) {
            Text("Join")
        }
    }
}