package com.taskmanager.ui.screen.taskdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Author: Hari K
 * Date: 06/03/2025.
 */
@Composable
fun TaskActionButtons(
    isCompleted: Boolean,
    onToggleComplete: () -> Unit,
    onDeleteTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onToggleComplete,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = if (isCompleted) Icons.Default.Refresh else Icons.Default.Done,
                contentDescription = if (isCompleted) "Mark Incomplete" else "Mark Complete"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isCompleted) "Mark Incomplete" else "Mark Complete")
        }

        Button(
            onClick = onDeleteTask,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delete")
        }
    }
}