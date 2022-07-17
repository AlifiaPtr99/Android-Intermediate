package com.dicoding.bangkit.storyappdicoding.activity.viewmodel

import android.content.ContentValues
import android.os.Build
import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.dicoding.bangkit.storyappdicoding.activity.api.ApiConfig
import com.dicoding.bangkit.storyappdicoding.activity.models.ItemStory
import com.dicoding.bangkit.storyappdicoding.activity.models.ListStoryResponse
import com.dicoding.bangkit.storyappdicoding.activity.models.UserSession
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val pref : TokenPreference) : ViewModel() {

        val listStory = MutableLiveData<List<ItemStory>>()

        fun getStory(): LiveData<List<ItemStory>> {
            return listStory
        }

        fun setListStory(token: String) {
            val client = ApiConfig.getApiService().getStoryWithMaps(token)
            client.enqueue(object : Callback<ListStoryResponse> {
                override fun onResponse(call: Call<ListStoryResponse>, response: Response<ListStoryResponse>) {
                    if (response.isSuccessful) {
                        listStory.postValue(response.body()?.listStory)
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }
                @RequiresApi(Build.VERSION_CODES.R)
                override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                    Log.e(ControlsProviderService.TAG, "Failure: ${t.message}")
                }
            })
        }

        fun getUserSession(): LiveData<UserSession> {
            return pref.getToken().asLiveData()
        }

    fun logout() {
        viewModelScope.launch {
            pref.removeToken()
        }
    }


}