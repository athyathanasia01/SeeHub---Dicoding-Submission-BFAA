package com.dicoding.seehub.model

import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.UserRepository
import com.dicoding.seehub.data.database.UserFavoriteEntity

class FavoriteViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun getListFavoriteUser() = userRepository.getAllFavoriteUser()

    fun deleteFromFavorite(user: UserFavoriteEntity) {
        userRepository.deleteFavoriteUser(user)
    }
}