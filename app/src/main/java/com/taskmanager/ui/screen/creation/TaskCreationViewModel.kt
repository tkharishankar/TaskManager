package com.taskmanager.ui.screen.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.data.repository.TaskRepository
import com.taskmanager.domain.model.Priority
import com.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Author: Hari K
 * Date: 05/03/2025.
 */
class TaskCreationViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TaskCreationState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<TaskCreationResult>()
    val events = _events.asSharedFlow()

    fun onEvent(event: TaskCreationEvent) {
        when (event) {
            is TaskCreationEvent.TitleChanged -> {
                _state.update { it.copy(title = event.title, titleError = false) }
            }

            is TaskCreationEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.description) }
            }

            is TaskCreationEvent.PriorityChanged -> {
                _state.update { it.copy(priority = event.priority) }
            }

            is TaskCreationEvent.DueDateChanged -> {
                _state.update { it.copy(dueDate = event.dueDate) }
            }

            is TaskCreationEvent.DatePickerRequested -> {
                _state.update { it.copy(showDatePicker = true) }
            }

            is TaskCreationEvent.DatePickerDismissed -> {
                _state.update { it.copy(showDatePicker = false) }
            }

            is TaskCreationEvent.CreateTaskClicked -> {
                validateAndCreateTask()
            }
        }
    }

    private fun validateAndCreateTask() {
        val currentState = _state.value

        if (currentState.title.isBlank()) {
            _state.update { it.copy(titleError = true) }
            return
        }

        viewModelScope.launch {
            try {
                val highestSequence = taskRepository.getHighestSequence() ?: 0
                val task = Task(
                    title = currentState.title,
                    description = currentState.description,
                    priorityValue = currentState.priority.value,
                    dueDate = currentState.dueDate,
                    sequence = highestSequence + 1
                )
                taskRepository.insertTask(task)
                _events.emit(TaskCreationResult.Success)
            } catch (e: Exception) {
                _events.emit(TaskCreationResult.Error(e.message ?: "Failed to create task"))
            }
        }
    }
}

data class TaskCreationState(
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.LOW,
    val dueDate: Date? = null,
    val showDatePicker: Boolean = false,
    val titleError: Boolean = false
)

sealed class TaskCreationEvent {
    data class TitleChanged(val title: String) : TaskCreationEvent()
    data class DescriptionChanged(val description: String) : TaskCreationEvent()
    data class PriorityChanged(val priority: Priority) : TaskCreationEvent()
    data class DueDateChanged(val dueDate: Date?) : TaskCreationEvent()
    object DatePickerRequested : TaskCreationEvent()
    object DatePickerDismissed : TaskCreationEvent()
    object CreateTaskClicked : TaskCreationEvent()
}

sealed class TaskCreationResult {
    object Success : TaskCreationResult()
    data class Error(val message: String) : TaskCreationResult()
}