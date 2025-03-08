package com.taskmanager.ui.screen.tasklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.data.repository.TaskRepository
import com.taskmanager.domain.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Author: Hari K
 * Date: 05/03/2025.
 */
class TaskListViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(TaskListViewState())
    val viewState = _viewState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            delay(1000)
            taskRepository.getAllTasks().collect { tasks ->
                _viewState.update { currentState ->
                    val updatedState = currentState.copy(
                        isLoading = false,
                        tasks = tasks
                    )
                    val filtered = when (updatedState.statusFilter) {
                        StatusFilter.ALL -> tasks
                        StatusFilter.PENDING -> tasks.filter { !it.isCompleted }
                        StatusFilter.COMPLETED -> tasks.filter { it.isCompleted }
                    }

                    val sorted = when (updatedState.sortOption) {
                        SortOption.NONE -> filtered.sortedBy { it.sequence }
                        SortOption.PRIORITY -> filtered.sortedByDescending { it.priority.ordinal }
                        SortOption.DUE_DATE -> filtered.sortedBy {
                            it.dueDate ?: Date(Long.MAX_VALUE)
                        }

                        SortOption.ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
                    }
                    updatedState.copy(filteredTasks = sorted)
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun undoDeleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun updateTasksOrder(reorderedTasks: List<Task>) {
        val currentState = _viewState.value
        if (currentState.sortOption != SortOption.NONE)
            return
        viewModelScope.launch {
            try {
                val tasksWithUpdatedSequence = reorderedTasks.mapIndexed { index, task ->
                    task.copy(sequence = index + 1)
                }
//                _viewState.update { it.copy(tasks = tasksWithUpdatedSequence) }
                taskRepository.updateTasksSequence(tasksWithUpdatedSequence)
            } catch (e: Exception) {
                Log.d("Sequence Update Error", "${e.message}")
            }
        }
    }

    fun setStatusFilter(filter: StatusFilter) {
        viewModelScope.launch {
            _viewState.update { it.copy(statusFilter = filter) }
            updateFilteredTasks()
        }
    }

    fun setSortOption(option: SortOption) {
        viewModelScope.launch {
            _viewState.update { it.copy(sortOption = option) }
            updateFilteredTasks()
        }
    }

    private fun updateFilteredTasks() {
        val currentState = _viewState.value
        val filtered = when (currentState.statusFilter) {
            StatusFilter.ALL -> currentState.tasks
            StatusFilter.PENDING -> currentState.tasks.filter { !it.isCompleted }
            StatusFilter.COMPLETED -> currentState.tasks.filter { it.isCompleted }
        }

        val sorted = when (currentState.sortOption) {
            SortOption.NONE -> filtered.sortedBy { it.sequence }
            SortOption.PRIORITY -> filtered.sortedByDescending { it.priority.ordinal }
            SortOption.DUE_DATE -> filtered.sortedBy { it.dueDate ?: Date(Long.MAX_VALUE) }
            SortOption.ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
        }

        _viewState.update { it.copy(filteredTasks = sorted) }
    }
}

data class TaskListViewState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val statusFilter: StatusFilter = StatusFilter.ALL,
    val sortOption: SortOption = SortOption.PRIORITY,
)

enum class StatusFilter(val displayName: String) {
    ALL("All"),
    PENDING("Pending"),
    COMPLETED("Completed")
}

enum class SortOption(val displayName: String) {
    PRIORITY("Priority"),
    DUE_DATE("Due Date"),
    ALPHABETICAL("A-Z"),
    NONE("None")
}