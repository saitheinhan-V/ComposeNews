package com.my.composenews.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsDataDTO(
    val articles: MutableList<NewsDTO> = mutableListOf(),
    val status: String = "",
    val totalResults: Int = 0
)
