package com.dicoding.seehub.data.retrofit

import com.dicoding.seehub.data.response.DetailResponse
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.data.response.UserListWithQueryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getListUsers() : Call<List<UserListResponseItem>>

    @GET("search/users")
    fun getListUsers(
        @Query("q") name: String
    ) : Call<UserListWithQueryResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") name: String
    ) : Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") name: String
    ) : Call<List<UserListResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") name: String
    ) : Call<List<UserListResponseItem>>
}