package com.dicoding.seehub.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

interface BaseUserItem {
    val id: Int
    val login: String
    val avatarUrl: String
    val isFavorite: Boolean
}

@Entity(tableName = "user_entity")
@Parcelize
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Int = 0,

    @ColumnInfo(name = "login")
    override val login: String,

    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String,

    @ColumnInfo(name = "isFavorite")
    override var isFavorite: Boolean
) : Parcelable, BaseUserItem

@Entity(tableName = "user_following_entity")
@Parcelize
data class UserFollowingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Int = 0,

    @ColumnInfo(name = "login")
    override val login: String,

    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String,

    @ColumnInfo(name = "isFavorite")
    override var isFavorite: Boolean
) : Parcelable, BaseUserItem

@Entity(tableName = "user_followers_entity")
@Parcelize
data class UserFollowersEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Int = 0,

    @ColumnInfo(name = "login")
    override val login: String,

    @ColumnInfo(name = "avatar_url")
    override val avatarUrl: String,

    @ColumnInfo(name = "isFavorite")
    override var isFavorite: Boolean
) : Parcelable, BaseUserItem

@Entity(tableName = "user_favorite_entity")
@Parcelize
data class UserFavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
) : Parcelable