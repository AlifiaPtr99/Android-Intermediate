package com.dicoding.bangkit.storyappdicoding.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.bangkit.storyappdicoding.api.ApiConfig
import com.dicoding.bangkit.storyappdicoding.model.ItemStory
import com.dicoding.bangkit.storyappdicoding.model.ListStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(): ViewModel() {
    val storyList = MutableLiveData<ArrayList<ItemStory>>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListStory(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStory("Bearer $token")
        client.enqueue(object: Callback<ListStoryResponse> {
            override fun onResponse(call: Call<ListStoryResponse>, response: Response<ListStoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    storyList.value = response.body()?.listStory!!
                }
            }
            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

}