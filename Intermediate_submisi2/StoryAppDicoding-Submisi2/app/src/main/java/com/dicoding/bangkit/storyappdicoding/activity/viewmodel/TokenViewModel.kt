package com.dicoding.bangkit.storyappdicoding.activity.viewmodel

import androidx.lifecycle.*
import com.dicoding.bangkit.storyappdicoding.activity.api.ApiService
import com.dicoding.bangkit.storyappdicoding.activity.models.UserSession
import kotlinx.coroutines.launch

class TokenViewModel(private val pref: TokenPreference) : ViewModel() {
    fun getTokens(): LiveData<UserSession> {
        return pref.getToken().asLiveData()
    }

    fun saveTokens(user : UserSession) {
        viewModelScope.launch {
            pref.saveToken(user)
        }
    }

    fun removeTokens() {
        viewModelScope.launch {
            pref.removeToken()
        }
    }



}

