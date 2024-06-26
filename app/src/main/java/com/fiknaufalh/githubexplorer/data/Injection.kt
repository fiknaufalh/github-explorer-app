package com.fiknaufalh.githubexplorer.data

import android.content.Context
import com.fiknaufalh.githubexplorer.data.local.room.FavoriteUserDatabase
import com.fiknaufalh.githubexplorer.data.local.SettingPreferences
import com.fiknaufalh.githubexplorer.data.local.dataStore
import com.fiknaufalh.githubexplorer.data.remote.retrofit.ApiConfig
import com.fiknaufalh.githubexplorer.utils.AppExecutors

class Injection {

    companion object {
        fun provideRepository(context: Context): MainRepository {
            val apiService = ApiConfig.getApiService()
            val database = FavoriteUserDatabase.getDatabase(context)
            val favoriteUserDao = database.favoriteUserDao()
            val pref = SettingPreferences.getInstance(context.dataStore)
            val appExecutors = AppExecutors()
            return MainRepository.getInstance(apiService, favoriteUserDao, pref, appExecutors)
        }
    }
}