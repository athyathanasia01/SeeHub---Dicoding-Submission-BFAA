package com.dicoding.seehub.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FollowFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FollowersViewModel::class.java)){
            @Suppress("UNCHECKED CAST")
            FollowersViewModel() as T
        } else if (modelClass.isAssignableFrom(FollowingViewModel::class.java)) {
            @Suppress("UNCHEKCED CAST")
            FollowingViewModel() as T
        } else {
            throw IllegalArgumentException("Unknown View Model Class")
        }
    }
}