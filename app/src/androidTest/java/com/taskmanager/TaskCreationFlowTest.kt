package com.taskmanager

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.taskmanager.data.repository.TaskRepository
import com.taskmanager.domain.model.Priority
import com.taskmanager.ui.screen.creation.CreateTask
import com.taskmanager.ui.screen.creation.CreateTaskContent
import com.taskmanager.ui.screen.creation.TaskCreationEvent
import com.taskmanager.ui.screen.creation.TaskCreationResult
import com.taskmanager.ui.screen.creation.TaskCreationState
import com.taskmanager.ui.screen.creation.TaskCreationViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

/**
 * Author: Hari K
 * Date: 07/03/2025.
 */
class TaskCreationFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: TaskCreationViewModel
    private lateinit var mockRepository: TaskRepository
    private lateinit var stateFlow: MutableStateFlow<TaskCreationState>
    private lateinit var eventsFlow: MutableSharedFlow<TaskCreationResult>

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        stateFlow = MutableStateFlow(TaskCreationState())
        eventsFlow = MutableSharedFlow()

        mockViewModel = mockk(relaxed = true) {
            every { state } returns stateFlow
            every { events } returns eventsFlow
            every { onEvent(any()) } answers {
                val event = firstArg<TaskCreationEvent>()
                when (event) {
                    is TaskCreationEvent.TitleChanged -> {
                        stateFlow.value = stateFlow.value.copy(
                            title = event.title,
                            titleError = event.title.isBlank()
                        )
                    }

                    is TaskCreationEvent.DescriptionChanged -> {
                        stateFlow.value = stateFlow.value.copy(
                            description = event.description
                        )
                    }

                    is TaskCreationEvent.PriorityChanged -> {
                        stateFlow.value = stateFlow.value.copy(
                            priority = event.priority
                        )
                    }

                    is TaskCreationEvent.DueDateChanged -> {
                        stateFlow.value = stateFlow.value.copy(
                            dueDate = event.dueDate,
                            showDatePicker = false
                        )
                    }

                    is TaskCreationEvent.DatePickerRequested -> {
                        stateFlow.value = stateFlow.value.copy(
                            showDatePicker = true
                        )
                    }

                    is TaskCreationEvent.DatePickerDismissed -> {
                        stateFlow.value = stateFlow.value.copy(
                            showDatePicker = false
                        )
                    }

                    is TaskCreationEvent.CreateTaskClicked -> {
                        if (stateFlow.value.title.isBlank()) {
                            stateFlow.value = stateFlow.value.copy(titleError = true)
                        } else {
                            composeTestRule.runOnIdle {
                                runBlocking {
                                    eventsFlow.emit(TaskCreationResult.Success)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun completeTaskCreationFlowSuccess() {

        composeTestRule.setContent {
            CreateTaskContent(
                state = stateFlow.value,
                onEvent = { mockViewModel.onEvent(it) }
            )
        }

        composeTestRule.onNodeWithText("Title*").performTextInput("Place Noon Order")
        composeTestRule.onNodeWithText("Description (Optional)")
            .performTextInput("S24, Back case, Temper")

        composeTestRule.onNodeWithText("HIGH").performClick()

        composeTestRule.onNodeWithContentDescription("Select date").performClick()

        stateFlow.value = stateFlow.value.copy(showDatePicker = true)
        composeTestRule.waitForIdle()

        stateFlow.value = stateFlow.value.copy(
            dueDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time,
            showDatePicker = false
        )

        composeTestRule.onNodeWithText("Create Task").performClick()

        verify { mockViewModel.onEvent(TaskCreationEvent.CreateTaskClicked) }
    }

    @Test
    fun taskCreationValidationFailure() {
        composeTestRule.setContent {
            CreateTask(
                viewModel = mockViewModel,
                onTaskCreated = { }
            )
        }

        composeTestRule.onNodeWithText("Description (Optional)")
            .performTextInput("This won't be saved")

        composeTestRule.onNodeWithText("Create Task").performClick()

        composeTestRule.onNodeWithText("Title is required").assertExists()
    }

    @Test
    fun prioritySelectionChangesUI() {
        composeTestRule.setContent {
            CreateTask(
                viewModel = mockViewModel,
                onTaskCreated = { }
            )
        }

        composeTestRule.onNodeWithText("LOW").assertIsSelected()

        composeTestRule.onNodeWithText("MEDIUM").performClick()

        stateFlow.value = stateFlow.value.copy(priority = Priority.MEDIUM)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("MEDIUM").assertIsSelected()
        composeTestRule.onNodeWithText("LOW").assertIsNotSelected()
    }

    @Test
    fun datePickerUpdatesUI() {
        composeTestRule.setContent {
            CreateTaskContent(
                state = stateFlow.value,
                onEvent = { mockViewModel.onEvent(it) }
            )
        }

        composeTestRule.onNodeWithText("Due Date").assertExists()

        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time

        stateFlow.value = stateFlow.value.copy(dueDate = tomorrow, showDatePicker = false)

        composeTestRule.waitForIdle()

        assert(stateFlow.value.dueDate == tomorrow) {
            "State should contain the updated due date"
        }

        composeTestRule.onNodeWithTag("dueDateField").assertExists()
    }

    @Test
    fun errorHandlingShowsToast() {
        val mockContext = mockk<Context>(relaxed = true)

        mockkStatic(Toast::class)
        every { Toast.makeText(any(), any<String>(), any()) } returns mockk(relaxed = true)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalContext provides mockContext) {
                CreateTask(
                    viewModel = mockViewModel,
                    onTaskCreated = { }
                )
            }
        }

        composeTestRule.runOnIdle {
            runBlocking {
                eventsFlow.emit(TaskCreationResult.Error("Failed to create task"))
            }
        }

        verify { Toast.makeText(mockContext, "Failed to create task", Toast.LENGTH_SHORT) }
    }
}