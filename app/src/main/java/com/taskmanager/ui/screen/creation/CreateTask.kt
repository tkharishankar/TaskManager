package com.taskmanager.ui.screen.creation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.taskmanager.ui.screen.creation.component.DueDateSelector
import com.taskmanager.ui.screen.creation.component.PrioritySelector
import com.taskmanager.ui.screen.creation.component.TaskDatePicker
import org.koin.androidx.compose.koinViewModel

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@Composable
fun CreateTask(
    viewModel: TaskCreationViewModel = koinViewModel(),
    onTaskCreated: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { result ->
            when (result) {
                is TaskCreationResult.Success -> {
                    onTaskCreated()
                }
                is TaskCreationResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CreateTaskContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun CreateTaskContent(
    state: TaskCreationState,
    onEvent: (TaskCreationEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Create New Task",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = { onEvent(TaskCreationEvent.TitleChanged(it)) },
            label = { Text("Title*") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.titleError
        )

        if (state.titleError) {
            Text(
                text = "Title is required",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        OutlinedTextField(
            value = state.description,
            onValueChange = { onEvent(TaskCreationEvent.DescriptionChanged(it)) },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        PrioritySelector(
            selectedPriority = state.priority,
            onPrioritySelected = { onEvent(TaskCreationEvent.PriorityChanged(it)) }
        )

        DueDateSelector(
            dueDate = state.dueDate,
            onDatePickerRequested = { onEvent(TaskCreationEvent.DatePickerRequested) }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onEvent(TaskCreationEvent.CreateTaskClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Create Task")
        }
    }

    if (state.showDatePicker) {
        TaskDatePicker(
            onDateSelected = { onEvent(TaskCreationEvent.DueDateChanged(it)) },
            onDismiss = { onEvent(TaskCreationEvent.DatePickerDismissed) }
        )
    }
}

