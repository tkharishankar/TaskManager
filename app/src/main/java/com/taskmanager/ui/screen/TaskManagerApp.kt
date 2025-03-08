package com.taskmanager.ui.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.taskmanager.ui.component.TaskAppBar
import com.taskmanager.ui.component.TaskBottomNavigation
import com.taskmanager.ui.component.TaskNavHost
import com.taskmanager.ui.screen.settings.SettingsViewModel
import com.taskmanager.ui.theme.TaskManagerTheme
import org.koin.androidx.compose.koinViewModel

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@Composable
fun TaskManagerApp(
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val darkMode by settingsViewModel.darkMode.collectAsState()
    val primaryColor by settingsViewModel.primaryColor.collectAsState()

    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var screenTitle by remember { mutableStateOf("") }
    var screenActions by remember { mutableStateOf<@Composable (RowScope.() -> Unit)>({}) }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(currentScreen) {
        if (currentScreen != Screen.Home) {
            screenActions = {}
        }
    }

    TaskManagerTheme(
        darkTheme = darkMode,
        customPrimaryColor = primaryColor
    ) {
        Scaffold(
            topBar = {
                TaskAppBar(
                    title = screenTitle,
                    actions = screenActions
                )
            },
            bottomBar = {
                TaskBottomNavigation(
                    navController = navController,
                    onScreenSelected = { newScreen ->
                        currentScreen = newScreen
                        if (newScreen != Screen.Home) {
                            screenTitle = newScreen.title
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { data ->
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        action = {
                            data.visuals.actionLabel?.let { actionLabel ->
                                TextButton(
                                    onClick = { data.performAction() },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.inversePrimary
                                    )
                                ) {
                                    Text(actionLabel)
                                }
                            }
                        }
                    ) {
                        Text(data.visuals.message)
                    }
                }
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                TaskNavHost(
                    navController = navController,
                    snackBarHostState = snackBarHostState,
                    darkMode = darkMode,
                    onDarkModeChange = settingsViewModel::setDarkMode,
                    primaryColor = primaryColor,
                    onColorSelected = settingsViewModel::setPrimaryColor,
                    onScreenSelected = { newScreen ->
                        currentScreen = newScreen
                        if (newScreen != Screen.Home) {
                            screenTitle = newScreen.title
                        }
                    },
                    onTitleChange = { newTitle ->
                        screenTitle = newTitle
                    },
                    onActionsChange = { newActions ->
                        screenActions = newActions
                    }
                )
            }
        }
    }
}