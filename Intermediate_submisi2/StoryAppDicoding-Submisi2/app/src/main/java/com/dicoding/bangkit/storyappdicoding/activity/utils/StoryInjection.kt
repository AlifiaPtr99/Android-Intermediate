package com.dicoding.bangkit.storyappdicoding.activity.utils


import com.dicoding.bangkit.storyappdicoding.activity.api.ApiConfig
import com.dicoding.bangkit.storyappdicoding.activity.repo.ListStoryRepository


object StoryInjection {
    fun provideRepository(): ListStoryRepository {
        val apiService = ApiConfig.getApiService()

        return ListStoryRepository(apiService)
    }
}