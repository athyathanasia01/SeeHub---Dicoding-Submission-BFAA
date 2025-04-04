package com.dicoding.seehub.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.seehub.data.response.DetailResponse
import com.dicoding.seehub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser : LiveData<DetailResponse> = _detailUser

    companion object {
        private val TAG = "UserViewmodel"
    }

    fun getDetailUser(name : String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(name)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailureResponse: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}", )
            }
        })
    }
}