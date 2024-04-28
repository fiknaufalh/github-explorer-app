package com.fiknaufalh.githubexplorer.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.fiknaufalh.githubexplorer.R
import com.fiknaufalh.githubexplorer.databinding.ActivityMainBinding
import com.fiknaufalh.githubexplorer.databinding.ActivitySettingBinding
import com.fiknaufalh.githubexplorer.utils.ViewModelFactory
import com.fiknaufalh.githubexplorer.viewmodels.MainViewModel
import com.fiknaufalh.githubexplorer.viewmodels.SettingViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel: SettingViewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        with (binding) {
            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                settingViewModel.saveThemeSetting(isChecked)
            }
            backTab.setOnClickListener { finish() }
        }
    }
}