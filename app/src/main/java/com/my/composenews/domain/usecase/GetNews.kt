package com.my.composenews.domain.usecase

import com.my.composenews.data.model.response.NewsDataDTO
import com.my.composenews.data.repository.NewsRepository
import com.my.composenews.data.resource.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNews @Inject constructor(
    private val repo: NewsRepository
) {
    suspend operator fun invoke(pageNumber: Int, category: String)
            : NetworkResponse<NewsDataDTO> {
        return  when (
            val result = repo.getNews(
                pageNumber, category
            )
        ) {
            is NetworkResponse.Failed -> {
                NetworkResponse.Failed(result.message, result.type)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(data = result.data)
            }
        }
    }
}