package com.taskmanager.ui.screen.creation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskmanager.domain.model.Priority
import com.taskmanager.ui.theme.priorityHighDark
import com.taskmanager.ui.theme.priorityHighLight
import com.taskmanager.ui.theme.priorityLowDark
import com.taskmanager.ui.theme.priorityLowLight
import com.taskmanager.ui.theme.priorityMediumDark
import com.taskmanager.ui.theme.priorityMediumLight

/**
 * Author: Hari K
 * Date: 08/03/2025.
 */
@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Text(
        text = "Priority",
        style = MaterialTheme.typography.bodyLarge
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Priority.entries.forEach { priorityOption ->
            PriorityChip(
                priority = priorityOption,
                selected = selectedPriority == priorityOption,
                onClick = { onPrioritySelected(priorityOption) }
            )
        }
    }
}

@Composable
fun PriorityChip(
    priority: Priority,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = when (priority) {
        Priority.LOW -> Pair(priorityLowLight, priorityLowDark)
        Priority.MEDIUM -> Pair(priorityMediumLight, priorityMediumDark)
        Priority.HIGH -> Pair(priorityHighLight, priorityHighDark)
    }

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(priority.name) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colors.first.copy(alpha = 0.2f),
            selectedLabelColor = colors.second
        )
    )
}
