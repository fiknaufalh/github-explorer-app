package com.fiknaufalh.githubexplorer.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubexplorer.data.responses.SearchResponse
import com.fiknaufalh.githubexplorer.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _users = MutableLiveData<SearchResponse>()
    val users: LiveData<SearchResponse> = _users

    private val _isMainLoading = MutableLiveData<Boolean>()
    val isMainLoading: LiveData<Boolean> = _isMainLoading

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _isSearched = MutableLiveData<Boolean>()
    val isSearched: LiveData<Boolean> = _isSearched

    init {
        findUsers("A")
        _isSearched.value = false
    }

    fun findUsers(q: String) {
        _isMainLoading.value = true
        val client = ApiConfig.getApiService().searchUser(q)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isMainLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()
                }
                else {
                    Log.d(TAG, "onFailResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isMainLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun changeQuery(q: String) {
        _searchQuery.value = q
    }

    fun setSearch(s: Boolean) {
        _isSearched.value = s
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
