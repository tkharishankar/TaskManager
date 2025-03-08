package com.taskmanager


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.material3.SnackbarHostState
import com.taskmanager.data.repository.TaskRepository
import com.taskmanager.domain.model.Priority
import com.taskmanager.domain.model.Task
import com.taskmanager.ui.screen.tasklist.Home
import com.taskmanager.ui.screen.tasklist.SortOption
import com.taskmanager.ui.screen.tasklist.TaskListViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

/**
 * Author: Hari K
 * Date: 08/03/2025.
 */
class TaskListFilterAndSortTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var taskRepository: TaskRepository
    private lateinit var viewModel: TaskListViewModel
    private val snackBarHostState = SnackbarHostState()

    @Before
    fun setup() {
        taskRepository = mockk(relaxed = true)

        val testTasks = listOf(
            Task(
                id = 1L,
                title = "Buy groceries",
                description = "Get milk, eggs, and bread",
                isCompleted = false,
                priorityValue = Priority.HIGH.value,
                dueDate = Date(System.currentTimeMillis() + 86400000),
                sequence = 1
            ),
            Task(
                id = 2L,
                title = "Call dentist",
                description = "Schedule annual checkup",
                isCompleted = true,
                priorityValue = Priority.MEDIUM.value,
                dueDate = Date(System.currentTimeMillis() + 172800000),
                sequence = 2
            ),
            Task(
                id = 3L,
                title = "Attend meeting",
                description = "Team standup at 10 AM",
                isCompleted = false,
                priorityValue = Priority.LOW.value,
                dueDate = Date(System.currentTimeMillis() + 43200000),
                sequence = 3
            )
        )

        every { taskRepository.getAllTasks() } returns flowOf(testTasks)

        viewModel = TaskListViewModel(taskRepository)
    }

    @Test
    fun testFilterTasksByStatus() {
        composeTestRule.setContent {
            Home(
                snackBarHostState = snackBarHostState,
                viewModel = viewModel,
                onTaskClick = {},
                onTaskDashboard = {},
                onTitleChange = {},
                onActionsChange = {}
            )
        }

//        verify(exactly = 1) { taskRepository.getAllTasks() }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
        composeTestRule.onNodeWithText("Call dentist").assertIsDisplayed()
        composeTestRule.onNodeWithText("Attend meeting").assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Filter Tasks")
            .performClick()

        composeTestRule
            .onNodeWithText("Filter Tasks")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("All").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pending").assertIsDisplayed()
        composeTestRule.onNodeWithText("Completed").assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Completed")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Call dentist")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Buy groceries")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Attend meeting")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription("Filter Tasks")
            .performClick()

        composeTestRule
            .onNodeWithText("Pending")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
        composeTestRule.onNodeWithText("Attend meeting").assertIsDisplayed()
        composeTestRule.onNodeWithText("Call dentist").assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription("Filter Tasks")
            .performClick()

        composeTestRule
            .onNodeWithText("All")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
        composeTestRule.onNodeWithText("Call dentist").assertIsDisplayed()
        composeTestRule.onNodeWithText("Attend meeting").assertIsDisplayed()
    }

    @Test
    fun testSortTasksByPriority() {
        composeTestRule.setContent {
            Home(
                snackBarHostState = snackBarHostState,
                viewModel = viewModel,
                onTaskClick = {},
                onTaskDashboard = {},
                onTitleChange = {},
                onActionsChange = { actionsContent ->
                }
            )
        }

        composeTestRule.waitForIdle()

        viewModel.setSortOption(SortOption.PRIORITY)

        composeTestRule.onAllNodesWithText("Buy groceries").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Call dentist").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Attend meeting").onFirst().assertIsDisplayed()

        fun SemanticsNodeInteraction.assertIsAbove(other: SemanticsNodeInteraction) {
            val myBottom = this.fetchSemanticsNode().positionInRoot.y + this.fetchSemanticsNode().size.height
            val otherTop = other.fetchSemanticsNode().positionInRoot.y

            if (myBottom > otherTop) {
                throw AssertionError("First node is not above second node")
            }
        }

        composeTestRule.onAllNodesWithText("Buy groceries").onFirst()
            .assertIsAbove(composeTestRule.onAllNodesWithText("Call dentist").onFirst())

        composeTestRule.onAllNodesWithText("Call dentist").onFirst()
            .assertIsAbove(composeTestRule.onAllNodesWithText("Attend meeting").onFirst())

        viewModel.setSortOption(SortOption.ALPHABETICAL)

        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithText("Attend meeting").onFirst()
            .assertIsAbove(composeTestRule.onAllNodesWithText("Buy groceries").onFirst())

        composeTestRule.onAllNodesWithText("Buy groceries").onFirst()
            .assertIsAbove(composeTestRule.onAllNodesWithText("Call dentist").onFirst())

        viewModel.setSortOption(SortOption.PRIORITY)

        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithText("Buy groceries").onFirst()
            .assertIsAbove(composeTestRule.onAllNodesWithText("Call dentist").onFirst())

        composeTestRule.onAllNodesWithText("Call dentist").onFirst()
            .assertIsAbove(composeTestRule.onAllNodesWithText("Attend meeting").onFirst())
    }
}