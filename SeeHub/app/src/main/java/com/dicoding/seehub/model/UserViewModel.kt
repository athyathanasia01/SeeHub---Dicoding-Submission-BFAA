package com.dicoding.seehub.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.data.response.UserListWithQueryResponse
import com.dicoding.seehub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<UserListResponseItem>>()
    val listUser: LiveData<List<UserListResponseItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = "UserViewmodel"
    }

    init {
        getListUser()
    }

    private fun getListUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers()
        client.enqueue(object : Callback<List<UserListResponseItem>> {
            override fun onResponse(call: Call<List<UserListResponseItem>>, response: Response<List<UserListResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _listUser.value = response.body()
                } else if(response.isSuccessful && response.body() == null){
                    _listUser.value = null
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

    fun getListUser(name : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUsers(name)
        client.enqueue(object : Callback<UserListWithQueryResponse> {
            override fun onResponse(
                call: Call<UserListWithQueryResponse>,
                response: Response<UserListWithQueryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null){
                    _listUser.value = response.body()?.items
                } else if (response.isSuccessful && response.body() == null) {
                    _listUser.value = null
                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserListWithQueryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}