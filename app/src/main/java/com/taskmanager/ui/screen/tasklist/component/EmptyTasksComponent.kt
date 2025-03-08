package com.taskmanager.ui.screen.tasklist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taskmanager.ui.screen.tasklist.StatusFilter

/**
 * Author: Hari K
 * Date: 06/03/2025.
 */
@Composable
fun EmptyTasksMessage(statusFilter: StatusFilter) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message = when (statusFilter) {
            StatusFilter.ALL -> "Ready to start organizing your day? Create your first task and begin your productivity journey."
            StatusFilter.PENDING -> "You've completed all your tasks! Take a moment to celebrate before adding more to your list."
            StatusFilter.COMPLETED -> "Once you complete tasks, they'll appear here. Keep going - progress is progress, no matter how small!"
        }
        Text(
            text = message,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
