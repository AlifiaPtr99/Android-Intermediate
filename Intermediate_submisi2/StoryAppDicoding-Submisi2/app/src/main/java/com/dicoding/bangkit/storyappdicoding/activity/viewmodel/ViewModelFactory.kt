package com.dicoding.bangkit.storyappdicoding.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.bangkit.storyappdicoding.activity.MainActivity
import com.dicoding.bangkit.storyappdicoding.activity.utils.StoryInjection
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.MainViewModel
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.StoryViewModel
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.TokenPreference
import com.dicoding.bangkit.storyappdicoding.activity.viewmodel.TokenViewModel

class ViewModelFactory(private val pref: TokenPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(StoryInjection.provideRepository()) as T
            }
            modelClass.isAssignableFrom(TokenViewModel::class.java) -> {
               TokenViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}