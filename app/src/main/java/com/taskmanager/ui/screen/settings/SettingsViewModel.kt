package com.taskmanager.ui.screen.settings

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.data.repository.SettingsRepository
import com.taskmanager.ui.theme.Gray80
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val darkMode = settingsRepository.getDarkModeState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val primaryColor = settingsRepository.getPrimaryColorState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, Gray80)

    fun setDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(isDarkMode)
        }
    }

    fun setPrimaryColor(color: Color) {
        viewModelScope.launch {
            settingsRepository.setPrimaryColor(color)
        }
    }
}