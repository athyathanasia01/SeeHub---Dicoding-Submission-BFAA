package com.dicoding.seehub.utils

import com.dicoding.seehub.data.database.BaseUserItem
import com.dicoding.seehub.data.database.UserFavoriteEntity
import com.dicoding.seehub.data.response.UserListResponseItem

fun BaseUserItem.toUserListResponseItem(): UserListResponseItem {
    return UserListResponseItem(
        "",
        "",
        "",
        "",
        "",
        this.login,
        "",
        "",
        "",
        "",
        "",
        this.avatarUrl,
        "",
        "",
        false,
        this.id,
        "",
        "",
        ""
    )
}

fun UserFavoriteEntity.toUserListResponseItem(): UserListResponseItem {
    return UserListResponseItem(
        "",
        "",
        "",
        "",
        "",
        this.login,
        "",
        "",
        "",
        "",
        "",
        this.avatarUrl,
        "",
        "",
        false,
        this.id,
        "",
        "",
        ""
    )
}