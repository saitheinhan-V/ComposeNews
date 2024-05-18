package com.my.composenews.presentation.view.detail

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.my.composenews.domain.vo.NewsVo

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreen(
    item: NewsVo = NewsVo()
){
    val webClient = remember {
        object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }
    }

    AndroidView(factory = {context ->
        WebView(context).apply {
            webViewClient = webClient
            settings.javaScriptEnabled = true
            loadUrl(item.url)
        }

    })
}