package com.fiknaufalh.githubexplorer.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubexplorer.data.responses.UserDetailResponse
import com.fiknaufalh.githubexplorer.data.responses.UserItem
import com.fiknaufalh.githubexplorer.data.retrofit.ApiConfig
import com.fiknaufalh.githubexplorer.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _followList = MutableLiveData<List<UserItem>>()
    val followList: LiveData<List<UserItem>> = _followList

    private val _isDetailLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isDetailLoading

    fun findDetail(q: String) {
        if (userDetail.value?.login != null) return

        _isDetailLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(q)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.d(TAG, "onResponseFail: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isDetailLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findFollow(type: String, username: String) {

        _isDetailLoading.value = true
        val client = if (type == "followers") {
            ApiConfig.getApiService().getFollowers(username)
        } else {
            ApiConfig.getApiService().getFollowing(username)
        }

        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    _followList.value = response.body()
                } else {
                    Log.d(TAG, "onFailResponse: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isDetailLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}