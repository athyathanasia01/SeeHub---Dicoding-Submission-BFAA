package com.dicoding.seehub.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _listFollowing = MutableLiveData<List<UserListResponseItem>?>()
    val listFollowers : LiveData<List<UserListResponseItem>?> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = "FollowingViewModel"
    }

    fun getListFollowing(name : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(name)
        client.enqueue(object : Callback<List<UserListResponseItem>> {
            override fun onResponse(call: Call<List<UserListResponseItem>>, response: Response<List<UserListResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listFollowing.value = response.body()
                } else if (response.isSuccessful && response.body() == null) {
                    _listFollowing.value = null
                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserListResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}