package com.dicoding.bangkit.storyappdicoding.activity.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.bangkit.storyappdicoding.activity.api.ApiService
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory

class ListStoryPagingSource(private val apiService: ApiService) : PagingSource<Int,ItemStory>() {


    override fun getRefreshKey(state: PagingState<Int, ItemStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemStory> {
        return try {
            val pageStory = params.key ?: INITIAL_PAGE_INDEX
            val storyResponse = apiService.getStory(
                HelperObject.token,
                pageStory,
                params.loadSize
            )
            val data = storyResponse.listStory
            LoadResult.Page(
                data = data,
                prevKey = if (pageStory == INITIAL_PAGE_INDEX) null else pageStory - 1,
                nextKey = if (data.isEmpty()) null else pageStory + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}