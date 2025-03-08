package com.taskmanager.data.repository

import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.taskmanager.ui.theme.Gray80
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
interface SettingsRepository {
    fun getDarkModeState(): Flow<Boolean>
    suspend fun setDarkMode(isDarkMode: Boolean)
    fun getPrimaryColorState(): Flow<Color>
    suspend fun setPrimaryColor(color: Color)
}

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private val THEME_MODE_KEY = booleanPreferencesKey("theme_mode")
    private val PRIMARY_COLOR_KEY = longPreferencesKey("primary_color")

    override fun getDarkModeState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_MODE_KEY] ?: false
        }
    }

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = isDarkMode
        }
    }

    override fun getPrimaryColorState(): Flow<Color> {
        return dataStore.data.map { preferences ->
            val colorValue = preferences[PRIMARY_COLOR_KEY]
            if (colorValue != null) {
                Color(colorValue.toULong())
            } else {
                Gray80
            }
        }
    }

    override suspend fun setPrimaryColor(color: Color) {
        dataStore.edit { preferences ->
            preferences[PRIMARY_COLOR_KEY] = color.value.toLong()
        }
    }
}