package com.dicoding.bangkit.storyappdicoding.activity.api


import com.dicoding.bangkit.storyappdicoding.activity.models.AddStoryResponse
import com.dicoding.bangkit.storyappdicoding.activity.models.ListStoryResponse
import com.dicoding.bangkit.storyappdicoding.activity.models.LoginResponse
import com.dicoding.bangkit.storyappdicoding.activity.models.RegisterResponse
import com.dicoding.bangkit.storyappdicoding.activity.utils.ListStoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
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
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = 0,
        @Query("size") size: Int? = 10
    ): ListStoryResponse


    @GET("stories")
    fun getStoryWithMaps(
        @Header("Authorization") token: String,
        @Query("location") loc: Int = 1
    ): Call<ListStoryResponse>
}