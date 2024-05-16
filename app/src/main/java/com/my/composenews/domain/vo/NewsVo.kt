package com.my.composenews.domain.vo

import com.my.composenews.data.model.response.NewsDTO
import com.my.composenews.data.model.response.Source
import java.io.Serializable


data class NewsVo(
    val author: String ?=null,
    val content: String?=null,
    val description: String?=null,
    var publishedAt: String = "",
    val source: Source = Source(),
    val title: String = "",
    val url: String = "",
    val urlToImage: String?=null
): Serializable