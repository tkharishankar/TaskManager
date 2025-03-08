package com.taskmanager.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.taskmanager.R

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
sealed class Screen(val route: String, val title: String, val icon: @Composable () -> Unit) {

    object Home : Screen(
        route = "home",
        title = "Home",
        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") }
    )

    object CreateTask : Screen(
        route = "create_task",
        title = "Create",
        icon = { Icon(Icons.Filled.Add, contentDescription = "Create Task") }
    )

    object TaskDetail : Screen(
        route = "task_detail/{taskId}",
        title = "Details",
        icon = { Icon(Icons.Outlined.Info, contentDescription = "Task Details") }
    ) {
        fun createRoute(taskId: Long) = "task_detail/$taskId"
    }

    object Dashboard : Screen(
        route = "dashboard",
        title = "Dashboard",
        icon = {
            Icon(
                painterResource(id = R.drawable.ic_dashboard),
                contentDescription = "Settings"
            )
        }
    )

    object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") }
    )
}
