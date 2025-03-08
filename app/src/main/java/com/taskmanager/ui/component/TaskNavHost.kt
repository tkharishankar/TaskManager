package com.taskmanager.ui.component

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.taskmanager.ui.screen.Screen
import com.taskmanager.ui.screen.creation.CreateTask
import com.taskmanager.ui.screen.dashboard.TaskDashboard
import com.taskmanager.ui.screen.settings.Settings
import com.taskmanager.ui.screen.taskdetail.TaskDetail
import com.taskmanager.ui.screen.tasklist.Home

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@Composable
fun TaskNavHost(
    navController: NavHostController,
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    primaryColor: Color,
    onColorSelected: (Color) -> Unit,
    onScreenSelected: (Screen) -> Unit,
    onTitleChange: (String) -> Unit,
    onActionsChange: (@Composable() (RowScope.() -> Unit)) -> Unit = {},
    snackBarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            Screen.Home.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseInOut
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOut
                    )
                )
            }
        ) {
            Home(
                snackBarHostState = snackBarHostState,
                onTaskClick = { taskId ->
                    onScreenSelected(Screen.TaskDetail)
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onTitleChange = onTitleChange,
                onActionsChange = onActionsChange,
                onTaskDashboard = {
                    onScreenSelected(Screen.Dashboard)
                    navController.navigate(Screen.Dashboard.route)
                }
            )
        }

        composable(
            Screen.CreateTask.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    initialOffsetX = { it }
                ) + fadeIn(
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { it }
                ) + fadeOut(
                    animationSpec = tween(200)
                )
            }
        ) {
            CreateTask(
                onTaskCreated = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Screen.TaskDetail.route,
            enterTransition = {
                scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    initialScale = 0.8f
                ) + fadeIn(
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            initialOffsetX = { -it / 3 }
                        )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { it }
                        )
            }
        ) { backStackEntry ->
            val taskId =
                backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
                    ?: 0
            TaskDetail(
                taskId = taskId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            Screen.Settings.route,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.Home.route -> {
                        slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { it / 2 }
                        ) + fadeIn(animationSpec = tween(500))
                    }

                    else -> {
                        slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { it }
                        ) + fadeIn(animationSpec = tween(500))
                    }
                }
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { it }
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            Settings(
                isDarkMode = darkMode,
                onDarkModeChange = onDarkModeChange,
                currentColor = primaryColor,
                onColorSelected = onColorSelected
            )
        }

        composable(
            Screen.Dashboard.route,
            enterTransition = {
                scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    initialScale = 0.8f
                ) + fadeIn(
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            initialOffsetX = { -it / 3 }
                        )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { it }
                        )
            }
        ) {
            TaskDashboard()
        }

    }
}