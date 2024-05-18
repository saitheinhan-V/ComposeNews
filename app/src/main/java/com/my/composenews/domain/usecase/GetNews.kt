package com.my.composenews.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.my.composenews.data.repository.NewsRepository
import com.my.composenews.domain.vo.NewsVo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNews @Inject constructor(
    private val repo: NewsRepository
) {
    suspend operator fun invoke(category: String)
            : Flow<PagingData<NewsVo>> {
//        return  when (
//            val result = repo.getNews(
//                pageNumber, category
//            )
//        ) {
//            is NetworkResponse.Failed -> {
//                NetworkResponse.Failed(result.message, result.type)
//            }
//
//            is NetworkResponse.Success -> {
//                NetworkResponse.Success(data = result.data)
//            }
//        }
        return repo.getNews(
            category = category
        ).map {
            it.map { dto ->
                dto.toVo(dto)
            }
        }
    }
}