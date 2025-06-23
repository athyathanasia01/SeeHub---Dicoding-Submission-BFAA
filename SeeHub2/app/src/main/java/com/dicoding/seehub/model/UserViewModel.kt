package com.dicoding.seehub.model

import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.UserRepository
import com.dicoding.seehub.data.database.UserEntity

class UserViewModel (private val userRepository: UserRepository) : ViewModel() {

    fun getListUser() = userRepository.getAllUser()

    fun getListUser(name: String) = userRepository.getUserBySearchName(name)

    fun insertUserFavorite(user: UserEntity) {
        userRepository.setUserFavorite(user, true)
    }

    fun deleteUserFavorite(user: UserEntity) {
        userRepository.setUserFavorite(user, false)
    }
}