package com.dicoding.seehub.model

import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.UserRepository
import com.dicoding.seehub.data.database.UserFollowingEntity

class FollowingViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getListFollowingUser(name: String) = userRepository.getUserFollowing(name)

    fun insertUserFollowingFavorite(user: UserFollowingEntity) {
        userRepository.setUserFollowingFavorite(user, true)
    }

    fun deleteUserFollowingFavorite(user: UserFollowingEntity) {
        userRepository.setUserFollowingFavorite(user, false)
    }
}