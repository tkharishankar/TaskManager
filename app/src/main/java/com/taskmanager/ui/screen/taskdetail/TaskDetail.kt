package com.taskmanager.ui.screen.taskdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskmanager.domain.model.Task
import com.taskmanager.ui.screen.taskdetail.component.DeleteConfirmationDialog
import com.taskmanager.ui.screen.taskdetail.component.ErrorDisplay
import com.taskmanager.ui.screen.taskdetail.component.TaskActionButtons
import com.taskmanager.ui.screen.taskdetail.component.TaskDescriptionSection
import com.taskmanager.ui.screen.taskdetail.component.TaskDetailHeader
import com.taskmanager.ui.screen.taskdetail.component.TaskDueDate
import com.taskmanager.ui.screen.taskdetail.component.TaskMetadata
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@Composable
fun TaskDetail(
    taskId: Long,
    onNavigateBack: () -> Unit,
    viewModel: TaskDetailViewModel = koinViewModel { parametersOf(taskId) },
) {
    val taskState by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is TaskDetailEvent.TaskDeleted, TaskDetailEvent.TaskUpdated -> onNavigateBack()
                is TaskDetailEvent.Error -> Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    if (taskState.showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onConfirm = { viewModel.onEvent(TaskDetailAction.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(TaskDetailAction.DismissDeleteDialog) }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (taskState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (taskState.error != null && taskState.task == null) {
            ErrorDisplay(
                message = taskState.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            taskState.task?.let { taskData ->
                TaskDetailContent(
                    task = taskData,
                    onToggleComplete = { viewModel.onEvent(TaskDetailAction.ToggleComplete) },
                    onDeleteTask = { viewModel.onEvent(TaskDetailAction.DeleteTask) }
                )
            }
        }
    }
}

@Composable
fun TaskDetailContent(
    task: Task,
    onToggleComplete: () -> Unit,
    onDeleteTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        TaskDetailHeader(task = task)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = task.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        TaskDueDate(dueDate = task.dueDate)

        TaskDescriptionSection(description = task.description)

        TaskMetadata(createdAt = task.createdAt, updatedAt = task.updatedAt)

        Spacer(modifier = Modifier.height(24.dp))

        TaskActionButtons(
            isCompleted = task.isCompleted,
            onToggleComplete = onToggleComplete,
            onDeleteTask = onDeleteTask
        )
    }
}










