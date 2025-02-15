package com.example.algorithms.data

import android.content.Context


//class TokenManager(private val context: Context) {
//    private val dataStore = context.createDataStore(name = "settings")
//
//    companion object {
//        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
//    }
//
//    suspend fun saveToken(token: String) {
//        dataStore.edit { preferences ->
//            preferences[TOKEN_KEY] = token
//        }
//    }
//
//    suspend fun getToken(): String? {
//        return dataStore.data.first()[TOKEN_KEY]
//    }
//
//    suspend fun deleteToken() {
//        dataStore.edit { preferences ->
//            preferences.remove(TOKEN_KEY)
//        }
//    }
//}


//Добавить в Koin
//val appModule = module {
//    // ... existing code ...
//    single { TokenManager(get()) }
//}