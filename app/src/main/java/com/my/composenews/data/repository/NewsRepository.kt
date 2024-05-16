package com.my.composenews.data.repository

import com.my.composenews.data.model.response.NewsDataDTO
import com.my.composenews.data.resource.NetworkResponse

interface NewsRepository {

    suspend fun getNews(
        pageNumber: Int,
        category: String
    ): NetworkResponse<NewsDataDTO>
}