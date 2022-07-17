package com.dicoding.bangkit.storyappdicoding.activity.models

data class UserSession(
    val name: String,
    val token: String,
    val isLogin: Boolean
)
