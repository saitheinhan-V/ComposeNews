package com.my.composenews.data.push

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.my.composenews.data.model.response.PushDTO
import com.my.composenews.data.repository.PushRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PushServiceHandlerImpl @Inject constructor(
    private val json: Json,
    private val pushRepo: PushRepository
) : PushServiceHandler {

    override fun onReceived(data: Map<String,String>, context: Context) {
//        val pushData = json.decodeFromString<PushDTO>(data)

        val json = Gson().toJson(data)
        val pushData = Gson().fromJson(json, PushDTO::class.java)
        pushRepo.notifyMessage(pushData.toVo())

    }
}