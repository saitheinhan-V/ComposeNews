package com.my.composenews.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.my.composenews.data.AppConstant
import com.my.composenews.data.model.response.NewsDTO
import com.my.composenews.data.service.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsListPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val category: String
) : PagingSource<Int, NewsDTO>() {
    override fun getRefreshKey(state: PagingState<Int, NewsDTO>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsDTO> {
        val currentPage = params.key ?: AppConstant.INITIAL_PAGE
        val pageSize = params.loadSize
        Log.i("current.page",currentPage.toString())
        return try {
            val response = apiService.getNews(
                countryCode = AppConstant.COUNTRY_CODE,
                pageNumber = currentPage,
                pageSize = pageSize,
                category = category
            )
            val article = response.body()?.articles.orEmpty()
            LoadResult.Page(
                data = article,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (article.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}