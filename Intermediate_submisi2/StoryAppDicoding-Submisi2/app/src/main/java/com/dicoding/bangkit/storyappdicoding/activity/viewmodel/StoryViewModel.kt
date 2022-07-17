package com.dicoding.bangkit.storyappdicoding.activity.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory
import com.dicoding.bangkit.storyappdicoding.activity.repo.ListStoryRepository
import com.dicoding.bangkit.storyappdicoding.activity.utils.StoryInjection


class StoryViewModel (private val repo: ListStoryRepository) : ViewModel() {
    fun storyUser(): LiveData<PagingData<ItemStory>> = repo.getUserStory().cachedIn(viewModelScope)
}

class FactoryViewModel(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) ->
                StoryViewModel(StoryInjection.provideRepository()) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

