package com.dicoding.bangkit.storyappdicoding.api

import com.dicoding.bangkit.storyappdicoding.model.AddStoryResponse
import com.dicoding.bangkit.storyappdicoding.model.ListStoryResponse
import com.dicoding.bangkit.storyappdicoding.model.LoginResponse
import com.dicoding.bangkit.storyappdicoding.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password : String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String,
    ): Call<ListStoryResponse>
}