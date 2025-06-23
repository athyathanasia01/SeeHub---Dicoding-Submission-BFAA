package com.dicoding.seehub.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.seehub.data.database.UserDao
import com.dicoding.seehub.data.database.UserEntity
import com.dicoding.seehub.data.database.UserFavoriteEntity
import com.dicoding.seehub.data.database.UserFollowersEntity
import com.dicoding.seehub.data.database.UserFollowingEntity
import com.dicoding.seehub.data.retrofit.ApiService
import com.dicoding.seehub.utils.AppExecutors
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.data.response.UserListWithQueryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors
){
    //UserEntity
    fun getAllUser() : LiveData<Result<List<UserEntity>>> {
        val result = MediatorLiveData<Result<List<UserEntity>>>()
        result.value = Result.Loading
        val client = apiService.getListUsers()
        client.enqueue(object : Callback<List<UserListResponseItem>> {
            override fun onResponse(
                call: Call<List<UserListResponseItem>?>,
                response: Response<List<UserListResponseItem>?>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val newUserList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { user ->
                            val isFavorite = userDao.checkIfUserFavorite(user.id)
                            val newUser = UserEntity(
                                user.id,
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )

                            newUserList.add(newUser)
                        }

                        userDao.deleteUserEntity()
                        userDao.insertUser(newUserList)
                    }
                }
            }

            override fun onFailure(
                call: Call<List<UserListResponseItem>?>,
                t: Throwable
            ) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = userDao.getAllUser()
        result.addSource(localData) { userNewData: List<UserEntity> ->
            result.value = Result.Success(userNewData)
        }

        return result
    }

    fun getUserBySearchName(name : String) : LiveData<Result<List<UserEntity>>> {
        val result = MediatorLiveData<Result<List<UserEntity>>>()
        result.value = Result.Loading
        val client = apiService.getListUsers(name)
        client.enqueue(object : Callback<UserListWithQueryResponse> {
            override fun onResponse(
                call: Call<UserListWithQueryResponse?>,
                response: Response<UserListWithQueryResponse?>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    val newUserList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { user ->
                            val isFavorite = userDao.checkIfUserFavorite(user.id)
                            val newUser = UserEntity(
                                user.id,
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )

                            newUserList.add(newUser)
                        }

                        userDao.deleteUserEntity()
                        userDao.insertUser(newUserList)
                    }
                }
            }

            override fun onFailure(
                call: Call<UserListWithQueryResponse?>,
                t: Throwable
            ) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = userDao.getAllUser()
        result.addSource(localData) { newUserData: List<UserEntity> ->
            result.value = Result.Success(newUserData)
        }

        return result
    }

    fun setUserFavorite(user: UserEntity, isFavorite: Boolean) {
        appExecutors.diskIO.execute {
            user.isFavorite = isFavorite
            userDao.updateUser(user)
        }

        val userFav = UserFavoriteEntity(
            user.id,
            user.login,
            user.avatarUrl
        )

        if (isFavorite) {
            insertFavoriteUser(userFav)
        } else {
            deleteFavoriteUser(userFav)
        }
    }

    //UserFollowingEntity
    fun getUserFollowing(user: String) : LiveData<Result<List<UserFollowingEntity>>> {
        val result = MediatorLiveData<Result<List<UserFollowingEntity>>>()
        result.value = Result.Loading
        val client = apiService.getFollowing(user)
        client.enqueue(object : Callback<List<UserListResponseItem>> {
            override fun onResponse(
                call: Call<List<UserListResponseItem>?>,
                response: Response<List<UserListResponseItem>?>
            ) {
                if (response.isSuccessful) {
                    val usersFollowing = response.body()
                    val newUserListFollowing = ArrayList<UserFollowingEntity>()
                    appExecutors.diskIO.execute {
                        usersFollowing?.forEach { user ->
                            val isFavorite = userDao.checkIfUserFavorite(user.id)
                            val newUserFollowing = UserFollowingEntity(
                                user.id,
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )

                            newUserListFollowing.add(newUserFollowing)
                        }

                        userDao.deleteFollowingEntity()
                        userDao.insertFollowing(newUserListFollowing)
                    }
                }
            }

            override fun onFailure(
                call: Call<List<UserListResponseItem>?>,
                t: Throwable
            ) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = userDao.getAllFollowingUser()
        result.addSource(localData) { newDataFollowing: List<UserFollowingEntity> ->
            result.value = Result.Success(newDataFollowing)
        }

        return result
    }

    fun setUserFollowingFavorite(user: UserFollowingEntity, isFavorite: Boolean) {
        appExecutors.diskIO.execute {
            user.isFavorite = isFavorite
            userDao.updateFollowing(user)
        }

        val userFav = UserFavoriteEntity(
            user.id,
            user.login,
            user.avatarUrl
        )

        if (isFavorite) {
            insertFavoriteUser(userFav)
        } else {
            deleteFavoriteUser(userFav)
        }
    }

    //UserFollowersEntity
    fun getUserFollowers(user: String) : LiveData<Result<List<UserFollowersEntity>>> {
        val result = MediatorLiveData<Result<List<UserFollowersEntity>>>()
        result.value = Result.Loading
        val client = apiService.getFollowers(user)
        client.enqueue(object : Callback<List<UserListResponseItem>> {
            override fun onResponse(
                call: Call<List<UserListResponseItem>?>,
                response: Response<List<UserListResponseItem>?>
            ) {
                if (response.isSuccessful) {
                    val usersFollowers = response.body()
                    val newUserListFollowers = ArrayList<UserFollowersEntity>()
                    appExecutors.diskIO.execute {
                        usersFollowers?.forEach { user ->
                            val isFavorite = userDao.checkIfUserFavorite(user.id)
                            val newUserFollowers = UserFollowersEntity(
                                user.id,
                                user.login,
                                user.avatarUrl,
                                isFavorite
                            )

                            newUserListFollowers.add(newUserFollowers)
                        }

                        userDao.deleteFollowersEntity()
                        userDao.insertFollowers(newUserListFollowers)
                    }
                }
            }

            override fun onFailure(
                call: Call<List<UserListResponseItem>?>,
                t: Throwable
            ) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = userDao.getAllFollowersUser()
        result.addSource(localData) { newDataFollowers: List<UserFollowersEntity> ->
            result.value = Result.Success(newDataFollowers)
        }

        return result
    }

    fun setUserFollowersFavorite(user: UserFollowersEntity, isFavorite: Boolean) {
        appExecutors.diskIO.execute {
            user.isFavorite = isFavorite
            userDao.updateFollowers(user)
        }

        val userFav = UserFavoriteEntity(
            user.id,
            user.login,
            user.avatarUrl
        )

        if (isFavorite) {
            insertFavoriteUser(userFav)
        } else {
            deleteFavoriteUser(userFav)
        }
    }

    //UserFavoriteEntity
    fun getAllFavoriteUser() : LiveData<List<UserFavoriteEntity>> {
        return userDao.getAllFavoriteUser()
    }

    private fun insertFavoriteUser(user: UserFavoriteEntity) {
        appExecutors.diskIO.execute {
            userDao.insertFavorite(user)
        }
    }

    fun deleteFavoriteUser(user: UserFavoriteEntity) {
        appExecutors.diskIO.execute {
            userDao.deleteUserFavorite(user.id)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getRepositoryInstance(
            apiService: ApiService,
            userDao: UserDao,
            appExecutors: AppExecutors
        ) : UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also { instance = it }
        }
    }
}