package com.my.composenews.data.agora

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.Objects


class TokenUtils {

    fun interface OnTokenGenCallback<T> {
        fun onTokenGen(ret: T)
    }

    companion object {
        private var client: OkHttpClient? = null
        private const val TAG = "TokenGenerator"


        init {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        }

        fun generate(
            appId: String?,
            certificate: String?,
            channelName: String?,
            uid: Int,
            onGetToken: OnTokenGenCallback<String?>?
        ) {
            generateToken(
                appId!!, certificate!!, channelName!!, uid,
                OnTokenGenCallback<String> { ret: String? ->
                    if (onGetToken != null) {
                        runOnUiThread { onGetToken.onTokenGen(ret) }
                    }
                }
            ) {
                Log.e(TAG, "for requesting token error, use config token instead.")
                if (onGetToken != null) {
                    runOnUiThread { onGetToken.onTokenGen(null) }
                }
            }
        }

        private fun runOnUiThread(runnable: Runnable) {
            if (Thread.currentThread() === Looper.getMainLooper().thread) {
                runnable.run()
            } else {
                Handler(Looper.getMainLooper()).post(runnable)
            }
        }

        private fun generateToken(
            appId: String,
            certificate: String,
            channelName: String,
            uid: Int,
            onGetToken: OnTokenGenCallback<String>?,
            onError: OnTokenGenCallback<Exception>?
        ) {
            if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(certificate) || TextUtils.isEmpty(
                    channelName
                )
            ) {
                onError?.onTokenGen(IllegalArgumentException("appId=$appId, certificate=$certificate, channelName=$channelName"))
                return
            }
            val postBody = JSONObject()
            try {
                postBody.put("appId", appId)
                postBody.put("appCertificate", certificate)
                postBody.put("channelName", channelName)
                postBody.put("expire", 360000) // s
                postBody.put("src", "Android")
                postBody.put("ts", System.currentTimeMillis().toString() + "")
                postBody.put("type", 1) // 1: RTC Token ; 2: RTM Token
                postBody.put("uid", uid.toString() + "")
            } catch (e: JSONException) {
                onError?.onTokenGen(e)
            }
            val request: Request = Request.Builder()
                .url("https://test-toolbox.bj2.agoralab.co/v1/token/generate")
                .addHeader("Content-Type", "application/json")
                .post(
                    postBody.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                )
//                .post(RequestBody.create(postBody.toString(), null))
                .build()
            client!!.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onError?.onTokenGen(e)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body
                    if (body != null) {
                        try {
                            val jsonObject = JSONObject(body.string())
                            val data = jsonObject.optJSONObject("data")
                            val token = Objects.requireNonNull(data).optString("token")
                            onGetToken?.onTokenGen(token)
                        } catch (e: Exception) {
                            onError?.onTokenGen(e)
                        }
                    }
                }
            })
        }
    }
}
