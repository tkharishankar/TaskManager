package com.taskmanager.ui.screen.tasklist.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.taskmanager.domain.model.Task
import com.taskmanager.ui.component.DismissBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

/**
 * Author: Hari K
 * Date: 06/03/2025.
 */
@Composable
fun TaskListComponent(
    tasks: List<Task>,
    onTaskClick: (Long) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onTasksReordered: (List<Task>) -> Unit,
) {
    var reorderingList by remember { mutableStateOf(tasks) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(tasks) {
        reorderingList = tasks
    }

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            reorderingList = reorderingList.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        },
        onDragEnd = { _, _ ->
            scope.launch {
                delay(100)
                onTasksReordered(reorderingList.toList())
            }
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .fillMaxSize()
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(
            items = reorderingList,
            key = { it.id }
        ) { task ->
            val currentItem by rememberUpdatedState(task)
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    when (it) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            onDeleteTask(currentItem)
                        }

                        SwipeToDismissBoxValue.Settled,
                        SwipeToDismissBoxValue.EndToStart,
                            -> return@rememberSwipeToDismissBoxState false
                    }
                    return@rememberSwipeToDismissBoxState true
                },
                positionalThreshold = { it * .5f }
            )

            ReorderableItem(
                state = state,
                key = task.id
            ) { isDragging ->

                val elevation = animateDpAsState(
                    if (isDragging) 8.dp else 0.dp,
                )

                Surface(
                    modifier = Modifier.shadow(elevation.value),
                    tonalElevation = elevation.value
                ) {
                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromEndToStart = false,
                        backgroundContent = { DismissBackground(dismissState) },
                        content = {
                            TaskItem(
                                task = task,
                                onClick = { onTaskClick(task.id) },
                            )
                        }
                    )
                }
            }
        }
    }
}