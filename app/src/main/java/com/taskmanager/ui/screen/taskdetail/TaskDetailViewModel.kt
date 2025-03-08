package com.taskmanager.ui.screen.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.data.repository.TaskRepository
import com.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Author: Hari K
 * Date: 05/03/2025.
 */
class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val taskId: Long
) : ViewModel() {
    private val _state = MutableStateFlow(TaskDetailState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<TaskDetailEvent>()
    val events = _events.asSharedFlow()

    init {
        loadTask()
    }

    fun onEvent(event: TaskDetailAction) {
        when (event) {
            is TaskDetailAction.ToggleComplete -> toggleTaskCompletion()
            is TaskDetailAction.DeleteTask -> deleteTask()
            is TaskDetailAction.ConfirmDelete -> handleConfirmDelete()
            is TaskDetailAction.DismissDeleteDialog -> _state.update { it.copy(showDeleteConfirmation = false) }
        }
    }

    private fun loadTask() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val task = taskRepository.getTaskById(taskId)
                _state.update {
                    it.copy(
                        task = task,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load task: ${e.message}"
                    )
                }
            }
        }
    }

    private fun toggleTaskCompletion() {
        val currentTask = _state.value.task ?: return
        viewModelScope.launch {
            try {
                val updatedTask = currentTask.copy(isCompleted = !currentTask.isCompleted)
                taskRepository.updateTask(updatedTask)
                _state.update { it.copy(task = updatedTask) }
                _events.emit(TaskDetailEvent.TaskUpdated)
            } catch (e: Exception) {
                _events.emit(TaskDetailEvent.Error("Failed to update task: ${e.message}"))
            }
        }
    }

    private fun deleteTask() {
        _state.update { it.copy(showDeleteConfirmation = true) }
    }

    private fun handleConfirmDelete() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showDeleteConfirmation = false) }
            try {
                taskRepository.deleteTaskById(taskId)
                _events.emit(TaskDetailEvent.TaskDeleted)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _events.emit(TaskDetailEvent.Error("Failed to delete task: ${e.message}"))
            }
        }
    }
}

data class TaskDetailState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmation: Boolean = false
)

sealed class TaskDetailAction {
    object ToggleComplete : TaskDetailAction()
    object DeleteTask : TaskDetailAction()
    object ConfirmDelete : TaskDetailAction()
    object DismissDeleteDialog : TaskDetailAction()
}

sealed class TaskDetailEvent {
    object TaskUpdated : TaskDetailEvent()
    object TaskDeleted : TaskDetailEvent()
    data class Error(val message: String) : TaskDetailEvent()
}