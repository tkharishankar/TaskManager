package com.taskmanager.ui.screen.taskdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.taskmanager.domain.model.Task

/**
 * Author: Hari K
 * Date: 06/03/2025.
 */
@Composable
fun TaskDetailHeader(task: Task, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PriorityBadge(priority = task.priority)
        StatusChip(isCompleted = task.isCompleted)
    }
}