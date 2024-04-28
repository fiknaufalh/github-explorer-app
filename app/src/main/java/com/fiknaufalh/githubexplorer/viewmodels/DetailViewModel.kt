package com.fiknaufalh.githubexplorer.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubexplorer.BuildConfig
import com.fiknaufalh.githubexplorer.data.remote.responses.UserDetailResponse
import com.fiknaufalh.githubexplorer.data.remote.responses.UserItem
import com.fiknaufalh.githubexplorer.data.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _followersList = MutableLiveData<List<UserItem>>()
    val followersList: LiveData<List<UserItem>> = _followersList

    private val _followingList = MutableLiveData<List<UserItem>>()
    val followingList: LiveData<List<UserItem>> = _followingList

    private val _isDetailLoading = MutableLiveData<Boolean>()
    val isDetailLoading: LiveData<Boolean> = _isDetailLoading

    private val _errorToast = MutableLiveData<Boolean?>()
    val errorToast: LiveData<Boolean?> = _errorToast

    fun findDetail(username: String) {
        _isDetailLoading.value = true
        val client = mainRepository.getUserDetail(username)

        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                    _errorToast.value = true
                } else {
                    if (BuildConfig.DEBUG) Log.d(TAG, "onResponseFail: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
                _isDetailLoading.value = false
                _errorToast.value = false
            }
        })
    }

    fun findFollowers(username: String) {
        _isDetailLoading.value = true
        val client = mainRepository.getFollowers(username)

        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    _followersList.value = response.body()
                    _errorToast.value = true
                } else {
                    if(BuildConfig.DEBUG) Log.d(TAG, "onResponseFail: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                if(BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
                _isDetailLoading.value = false
                _errorToast.value = false
            }
        })
    }

    fun findFollowing(username: String) {
        _isDetailLoading.value = true
        val client = mainRepository.getFollowing(username)

        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    _followingList.value = response.body()
                    _errorToast.value = true
                } else {
                    if(BuildConfig.DEBUG) Log.d(TAG, "onResponseFail: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                if(BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
                _isDetailLoading.value = false
                _errorToast.value = false
            }
        })
    }

    fun resetToast() {
        _errorToast.value = null
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}