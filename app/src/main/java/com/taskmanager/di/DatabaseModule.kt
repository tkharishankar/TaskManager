package com.taskmanager.di

import androidx.room.Room
import com.taskmanager.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Author: Hari K
 * Date: 05/03/2025.
 */
val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "task_database"
        ).build()
    }
    single { get<AppDatabase>().taskDao() }
}