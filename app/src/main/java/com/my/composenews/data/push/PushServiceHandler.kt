package com.my.composenews.data.push

import android.content.Context

interface PushServiceHandler {

    fun onReceived(data: Map<String,String>,context: Context)
}