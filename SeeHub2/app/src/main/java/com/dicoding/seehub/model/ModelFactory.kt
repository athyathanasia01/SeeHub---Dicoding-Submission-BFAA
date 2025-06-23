package com.dicoding.seehub.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.dicoding.seehub.data.UserRepository
import com.dicoding.seehub.dependency.Injection
import com.dicoding.seehub.model.preferences.ThemePreferences
import com.dicoding.seehub.model.preferences.ThemeViewModel

class ModelFactory private constructor(private val userRepository: UserRepository, private val preferences: ThemePreferences) : NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FollowingViewModel::class.java)) {
            return FollowingViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(preferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ModelFactory? = null
        fun getFactoryModelInstance(context: Context, application: Application) : ModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ModelFactory(Injection.provideRepository(context), Injection.provideTheme(application))
            }.also { instance = it }
        }
    }
}