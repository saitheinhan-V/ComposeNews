package com.my.composenews.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.composenews.data.AppConstant
import com.my.composenews.data.model.response.NewsDTO
import com.my.composenews.data.model.response.NewsDataDTO
import com.my.composenews.data.paging.NewsListPagingSource
import com.my.composenews.data.resource.NetworkResponse
import com.my.composenews.data.resource.safeApiCall
import com.my.composenews.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): NewsRepository{

    //    override suspend fun getNews(pageNumber: Int, category: String)
//    : NetworkResponse<NewsDataDTO> {
//        return withContext(Dispatchers.IO){
//            safeApiCall {
//                apiService.getNews(
//                    AppConstant.COUNTRY_CODE,
//                    pageNumber,
//                    AppConstant.PAGE_SIZE,
//                    category
//                )
//            }
//        }
//    }
    override suspend fun getNews(category: String)
    : Flow<PagingData<NewsDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = AppConstant.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsListPagingSource(
                    apiService = apiService,
                    category = category
                )
            }
        ).flow
    }


}