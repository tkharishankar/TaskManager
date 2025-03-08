package com.taskmanager.di

import com.taskmanager.data.repository.RoomTaskRepository
import com.taskmanager.data.repository.TaskRepository
import com.taskmanager.ui.screen.creation.TaskCreationViewModel
import com.taskmanager.ui.screen.taskdetail.TaskDetailViewModel
import com.taskmanager.ui.screen.tasklist.TaskListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Author: Hari K
 * Date: 05/03/2025.
 */
val taskModule = module {

    single<TaskRepository> { RoomTaskRepository(get()) }

    viewModel { TaskListViewModel(get()) }

    viewModel { TaskCreationViewModel(get()) }

    viewModel { parameters -> TaskDetailViewModel(get(), parameters.get()) }
}