package com.taskmanager.ui.screen.tasklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.taskmanager.R
import com.taskmanager.domain.model.Task
import com.taskmanager.ui.component.bounceEffect
import com.taskmanager.ui.screen.tasklist.component.EmptyTasksMessage
import com.taskmanager.ui.screen.tasklist.component.FilterBottomSheetContent
import com.taskmanager.ui.screen.tasklist.component.ShimmerTaskList
import com.taskmanager.ui.screen.tasklist.component.TaskListComponent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    snackBarHostState: SnackbarHostState,
    viewModel: TaskListViewModel = koinViewModel(),
    onTaskClick: (Long) -> Unit,
    onTaskDashboard: () -> Unit,
    onTitleChange: (String) -> Unit,
    onActionsChange: (@Composable() (RowScope.() -> Unit)) -> Unit,
) {
    val viewState by viewModel.viewState.collectAsState()

    val scope = rememberCoroutineScope()

    var lastDeletedTask by remember { mutableStateOf<Task?>(null) }

    val filterSheetState = rememberModalBottomSheetState()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sortMenuExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(viewState.statusFilter) {
        onTitleChange("${viewState.statusFilter.displayName} Tasks")
    }

    LaunchedEffect(viewModel) {
        onActionsChange {
            IconButton(onClick = { onTaskDashboard() }) {
                Icon(
                    painterResource(id = R.drawable.ic_dashboard),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Settings"
                )
            }

            Box {
                IconButton(onClick = { sortMenuExpanded.value = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Sort Tasks"
                    )
                }

                DropdownMenu(
                    expanded = sortMenuExpanded.value,
                    onDismissRequest = { sortMenuExpanded.value = false }
                ) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.displayName) },
                            onClick = {
                                viewModel.setSortOption(option)
                                sortMenuExpanded.value = false
                            },
                            leadingIcon = {
                                if (viewState.sortOption == option) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    val onDeleteTask: (Task) -> Unit = { task ->
        lastDeletedTask = task
        viewModel.deleteTask(task)
        scope.launch {
            val result = snackBarHostState.showSnackbar(
                message = "Task deleted",
                actionLabel = "UNDO",
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                lastDeletedTask?.let { deletedTask ->
                    viewModel.undoDeleteTask(deletedTask)
                    lastDeletedTask = null
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewState.isLoading) {
                ShimmerTaskList()
            } else if (viewState.filteredTasks.isEmpty()) {
                EmptyTasksMessage(viewState.statusFilter)
            } else {
                TaskListComponent(
                    tasks = viewState.filteredTasks,
                    onTaskClick = onTaskClick,
                    onDeleteTask = onDeleteTask,
                    onTasksReordered = viewModel::updateTasksOrder,
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .bounceEffect(),
            onClick = { showFilterSheet = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_list),
                tint = MaterialTheme.colorScheme.onSecondary,
                contentDescription = "Filter Tasks"
            )
        }
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = filterSheetState
        ) {
            FilterBottomSheetContent(
                currentFilter = viewState.statusFilter,
                onFilterChange = {
                    viewModel.setStatusFilter(it)
                    showFilterSheet = false
                }
            )
        }
    }
}
