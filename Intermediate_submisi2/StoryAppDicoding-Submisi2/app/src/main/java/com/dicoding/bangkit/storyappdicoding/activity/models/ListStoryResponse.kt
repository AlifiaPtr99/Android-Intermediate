package com.dicoding.bangkit.storyappdicoding.activity.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ListStoryResponse(
    @field:SerializedName("error")
    val error: Boolean?,

    @field:SerializedName("message")
    val message: String?,

    @field:SerializedName("listStory")
    val listStory: List<ItemStory>
)

@Parcelize
data class ItemStory(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description : String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("lat")
    val lat: Double,
) : Parcelable