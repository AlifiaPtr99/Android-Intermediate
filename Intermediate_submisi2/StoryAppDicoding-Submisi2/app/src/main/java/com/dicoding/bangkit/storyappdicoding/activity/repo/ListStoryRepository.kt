package com.dicoding.bangkit.storyappdicoding.activity.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.bangkit.storyappdicoding.activity.api.ApiService
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory
import com.dicoding.bangkit.storyappdicoding.activity.utils.ListStoryPagingSource

class ListStoryRepository (private val apiService: ApiService) {
    fun getUserStory(): LiveData<PagingData<ItemStory>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                ListStoryPagingSource(apiService)
            }).liveData
    }
}