package com.dicoding.seehub.model

import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.UserRepository
import com.dicoding.seehub.data.database.UserFollowersEntity

class FollowersViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun getListFollowersUser(name: String) = userRepository.getUserFollowers(name)

    fun insertUserFollowersFavorite(user: UserFollowersEntity) {
        userRepository.setUserFollowersFavorite(user, true)
    }

    fun deleteUserFollowersFavorite(user: UserFollowersEntity) {
        userRepository.setUserFollowersFavorite(user, false)
    }
}