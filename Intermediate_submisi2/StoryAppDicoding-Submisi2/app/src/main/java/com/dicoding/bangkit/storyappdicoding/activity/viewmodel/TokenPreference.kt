package com.dicoding.bangkit.storyappdicoding.activity.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.bangkit.storyappdicoding.activity.models.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenPreference private constructor(private val dataStore: DataStore<Preferences>) {


    fun getToken(): Flow<UserSession> {
        return dataStore.data.map { preferences ->
            UserSession(
                preferences[NAME_KEY] ?:"",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveToken(user : UserSession) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun removeToken() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences.remove(STATE_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TokenPreference? = null
        private val NAME_KEY = stringPreferencesKey("name")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): TokenPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = TokenPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}