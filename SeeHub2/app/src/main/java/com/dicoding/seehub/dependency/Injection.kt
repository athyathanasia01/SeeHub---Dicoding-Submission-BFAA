package com.dicoding.seehub.dependency

import android.app.Application
import android.content.Context
import com.dicoding.seehub.data.UserRepository
import com.dicoding.seehub.data.database.UserDatabase
import com.dicoding.seehub.data.retrofit.ApiConfig
import com.dicoding.seehub.model.preferences.ThemePreferences
import com.dicoding.seehub.model.preferences.dataStore
import com.dicoding.seehub.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context) : UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getDatabaseInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getRepositoryInstance(apiService, dao, appExecutors)
    }

    fun provideTheme(application: Application) : ThemePreferences {
        return ThemePreferences.getThemeInstance(application.dataStore)
    }
}