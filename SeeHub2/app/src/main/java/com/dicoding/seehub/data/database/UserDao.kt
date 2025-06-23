package com.dicoding.seehub.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    //UserEntity
    //Ini untuk fetch semua data dari API masukin ke database
    @Query("SELECT * FROM user_entity ORDER BY id DESC")
    fun getAllUser() : LiveData<List<UserEntity>>

    //Ini untuk insert data ke dalam database
    //catatan: Data dari fetch halaman utama dimasukkan semua (favorite dan unfavorite)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: List<UserEntity>)

    //selalu update ketika terjadi perubahan pada database favorite user
    @Update
    fun updateUser(user: UserEntity)

    //hapus user entity, untuk load data baru
    @Query("DELETE FROM user_entity")
    fun deleteUserEntity()

    //FollowingEntity
    @Query("SELECT * FROM user_following_entity ORDER BY id DESC")
    fun getAllFollowingUser() : LiveData<List<UserFollowingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowing(followingUser: List<UserFollowingEntity>)

    @Update
    fun updateFollowing(followingUser: UserFollowingEntity)

    @Query("DELETE FROM user_following_entity")
    fun deleteFollowingEntity()

    //FollowersEntity
    @Query("SELECT * FROM user_followers_entity ORDER BY id DESC")
    fun getAllFollowersUser() : LiveData<List<UserFollowersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFollowers(followingUser: List<UserFollowersEntity>)

    @Update
    fun updateFollowers(followersUser: UserFollowersEntity)

    @Query("DELETE FROM user_followers_entity")
    fun deleteFollowersEntity()

    //FavoriteUserEntity
    @Query("SELECT * FROM user_favorite_entity ORDER BY id DESC")
    fun getAllFavoriteUser() : LiveData<List<UserFavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteUser: UserFavoriteEntity)

    @Query("DELETE FROM user_favorite_entity WHERE id = :id")
    fun deleteUserFavorite(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM user_favorite_entity WHERE id = :id)")
    fun checkIfUserFavorite(id: Int) : Boolean
}