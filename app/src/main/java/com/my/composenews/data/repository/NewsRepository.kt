package com.my.composenews.data.repository

import androidx.paging.PagingData
import com.my.composenews.data.model.response.NewsDTO
import com.my.composenews.data.model.response.NewsDataDTO
import com.my.composenews.data.resource.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(
        category: String
    ): Flow<PagingData<NewsDTO>>
}