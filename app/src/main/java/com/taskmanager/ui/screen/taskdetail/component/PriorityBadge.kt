package com.taskmanager.ui.screen.taskdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taskmanager.domain.model.Priority
import com.taskmanager.ui.theme.priorityHighDark
import com.taskmanager.ui.theme.priorityLowDark
import com.taskmanager.ui.theme.priorityMediumDark

/**
 * Author: Hari K
 * Date: 06/03/2025.
 */
@Composable
fun PriorityBadge(priority: Priority) {
    val (color, text) = when (priority) {
        Priority.HIGH -> priorityHighDark to "High Priority"
        Priority.MEDIUM -> priorityMediumDark to "Medium Priority"
        Priority.LOW -> priorityLowDark to "Low Priority"
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
    }
}