package com.my.composenews.data.service

import com.my.composenews.data.AppConstant
import com.my.composenews.data.model.response.NewsDataDTO
import com.my.composenews.data.resource.NetworkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("X-Api-Key: ${AppConstant.API_KEY}")
    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String ?= AppConstant.COUNTRY_CODE,
        @Query("page")
        pageNumber: Int = 1,
        @Query("pageSize")
        pageSize: Int = AppConstant.PAGE_SIZE,
        @Query("category")
        category: String = AppConstant.CATEGORY
    ): Response<NewsDataDTO>
}