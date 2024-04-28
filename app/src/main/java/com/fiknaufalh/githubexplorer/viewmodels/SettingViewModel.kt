package com.fiknaufalh.githubexplorer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fiknaufalh.githubexplorer.data.MainRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return mainRepository.getThemeSettings().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            mainRepository.saveThemeSettings(isDarkModeActive)
        }
    }
}