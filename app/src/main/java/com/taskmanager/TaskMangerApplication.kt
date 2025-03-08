package com.taskmanager

import android.app.Application
import com.taskmanager.di.databaseModule
import com.taskmanager.di.settingsModule
import com.taskmanager.di.taskModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
class TaskMangerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TaskMangerApplication)
            modules(
                databaseModule,
                settingsModule,
                taskModule
            )
        }
    }
}