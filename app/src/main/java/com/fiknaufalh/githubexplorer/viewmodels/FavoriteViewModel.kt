package com.fiknaufalh.githubexplorer.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiknaufalh.githubexplorer.data.local.entity.FavoriteUser
import com.fiknaufalh.githubexplorer.data.MainRepository

class FavoriteViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _favoriteUsers = MutableLiveData<List<FavoriteUser>>()
    val favoriteUsers :LiveData<List<FavoriteUser>> = _favoriteUsers

    init {
        mainRepository.getAllFavoriteUsers().observeForever {
            favoriteList -> _favoriteUsers.value = favoriteList
        }
    }

    fun isFavorite(favoriteUser: FavoriteUser): Boolean {
        return favoriteUsers.value?.any { it.username == favoriteUser.username } ?: false
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mainRepository.getAllFavoriteUsers()

    fun getFavoriteUserByUsername(username: String) = mainRepository.getFavoriteUserByUsername(username)

    fun insertFavorite(favoriteUser: FavoriteUser) {
        mainRepository.insertFavorite(favoriteUser)
    }

    fun deleteFavorite(favoriteUser: FavoriteUser) {
        mainRepository.deleteFavorite(favoriteUser)
    }
}