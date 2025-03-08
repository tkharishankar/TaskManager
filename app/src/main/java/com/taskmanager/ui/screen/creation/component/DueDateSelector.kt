package com.taskmanager.ui.screen.creation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Author: Hari K
 * Date: 08/03/2025.
 */
@Composable
fun DueDateSelector(
    dueDate: Date?,
    onDatePickerRequested: () -> Unit
) {
    val formattedDate = remember(dueDate) {
        dueDate?.let { date ->
            val localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            localDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        } ?: ""
    }

    OutlinedTextField(
        value = formattedDate,
        onValueChange = { },
        label = { Text("Due Date") },
        modifier = Modifier.fillMaxWidth().testTag("dueDateField"),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onDatePickerRequested) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        }
    )
}