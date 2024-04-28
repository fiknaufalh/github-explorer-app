package com.fiknaufalh.githubexplorer.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubexplorer.BuildConfig
import com.fiknaufalh.githubexplorer.data.remote.responses.SearchResponse
import com.fiknaufalh.githubexplorer.data.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    private val _users = MutableLiveData<SearchResponse>()
    val users: LiveData<SearchResponse> = _users

    private val _isMainLoading = MutableLiveData<Boolean>()
    val isMainLoading: LiveData<Boolean> = _isMainLoading

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _isSearched = MutableLiveData<Boolean>()
    val isSearched: LiveData<Boolean> = _isSearched

    private val _errorToast = MutableLiveData<Boolean?>()
    val errorToast: LiveData<Boolean?> = _errorToast

    init {
        findUsers("A")
        _isSearched.value = false
    }

    fun findUsers(q: String) {
        _isMainLoading.value = true
        val client = mainRepository.searchUser(q)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isMainLoading.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()
                    _errorToast.value = true
                }
                else {
                    if (BuildConfig.DEBUG) Log.d(TAG, "onFailResponse: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                if (BuildConfig.DEBUG) Log.d(TAG, "onFailure: ${t.message}")
                _isMainLoading.value = false
                _errorToast.value = false
            }
        })
    }

    fun changeQuery(q: String) {
        _searchQuery.value = q
    }

    fun setSearch(s: Boolean) {
        _isSearched.value = s
    }

    fun resetToast() {
        _errorToast.value = null
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
