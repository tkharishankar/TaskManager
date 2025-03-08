package com.taskmanager.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.taskmanager.data.repository.DataStoreSettingsRepository
import com.taskmanager.data.repository.SettingsRepository
import com.taskmanager.ui.screen.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
val settingsModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("task_prefs") }
        )
    }

    single<SettingsRepository> { DataStoreSettingsRepository(get()) }

    viewModel { SettingsViewModel(get()) }
}