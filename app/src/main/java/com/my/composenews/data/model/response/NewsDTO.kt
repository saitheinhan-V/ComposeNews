package com.my.composenews.data.model.response

import com.my.composenews.domain.vo.NewsVo
import kotlinx.serialization.Serializable

@Serializable
data class NewsDTO(
    val author: String ?= null,
    val content: String ?= null,
    val description: String ?= null,
    var publishedAt: String,
    val source: Source = Source(),
    val title: String,
    val url: String,
    val urlToImage: String ?= null
) {
    fun toVo(dto: NewsDTO) : NewsVo {
        return NewsVo(
            author = dto.author,
            content = dto.content,
            description = dto.description,
            publishedAt = dto.publishedAt,
            source = dto.source,
            title = dto.title,
            url = dto.url,
            urlToImage = dto.urlToImage
        )
    }
}