package com.fiknaufalh.githubexplorer.data

import androidx.lifecycle.LiveData
import com.fiknaufalh.githubexplorer.data.local.entity.FavoriteUser
import com.fiknaufalh.githubexplorer.data.local.room.FavoriteUserDao
import com.fiknaufalh.githubexplorer.data.local.SettingPreferences
import com.fiknaufalh.githubexplorer.data.remote.retrofit.ApiService
import com.fiknaufalh.githubexplorer.utils.AppExecutors
import kotlinx.coroutines.flow.Flow

class MainRepository private constructor (
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val settingPreferences: SettingPreferences,
    private val appExecutors: AppExecutors
) {

    fun searchUser(query: String) = apiService.searchUser(query)

    fun getUserDetail(username: String) = apiService.getUserDetail(username)

    fun getFollowers(username: String) = apiService.getFollowers(username)

    fun getFollowing(username: String) = apiService.getFollowing(username)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUser()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }
    fun insertFavorite(favoriteUser: FavoriteUser) {
        appExecutors.diskIO.execute {
            favoriteUserDao.insert(favoriteUser)
        }
    }

    fun deleteFavorite(favoriteUser: FavoriteUser) {
        appExecutors.diskIO.execute {
            favoriteUserDao.delete(favoriteUser)
        }
    }

    fun getThemeSettings(): Flow<Boolean> = settingPreferences.getThemeSetting()

    suspend fun saveThemeSettings(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
            settingPreferences: SettingPreferences,
            appExecutors: AppExecutors
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(
                    apiService,
                    favoriteUserDao,
                    settingPreferences,
                    appExecutors)
            }.also { instance = it }
    }
}