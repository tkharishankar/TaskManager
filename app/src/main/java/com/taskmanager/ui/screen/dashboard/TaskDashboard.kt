package com.taskmanager.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taskmanager.R
import com.taskmanager.ui.screen.tasklist.TaskListViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Author: Hari K
 * Date: 08/03/2025.
 */
@Composable
fun TaskDashboard(
    viewModel: TaskListViewModel = koinViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()

    val completedTasks = viewState.tasks.count { it.isCompleted }
    val totalTasks = viewState.tasks.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Task Progress",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TaskCompletionProgress(
            completedTasks = completedTasks,
            totalTasks = totalTasks,
            modifier = Modifier.size(200.dp),
            progressColor = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticCard(
                title = "Total",
                value = totalTasks.toString(),
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_check_circle_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            StatisticCard(
                title = "Completed",
                value = completedTasks.toString(),
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            val pendingTasks = totalTasks - completedTasks
            StatisticCard(
                title = "Pending",
                value = pendingTasks.toString(),
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_pending),
                        contentDescription = null,
                        tint = if (pendingTasks > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                    )
                }
            )
        }
    }
}

@Composable
fun StatisticCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Box(
//                modifier = Modifier
//                    .padding(bottom = 8.dp)
//            ) {
//                icon()
//            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}