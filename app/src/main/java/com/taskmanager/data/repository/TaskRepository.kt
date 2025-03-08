package com.taskmanager.data.repository

import com.taskmanager.data.local.TaskDao
import com.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTaskById(taskId: Long): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(taskId: Long)
    suspend fun getHighestSequence(): Int?
    suspend fun updateTasksSequence(tasks: List<Task>)
}

class RoomTaskRepository(
    private val taskDao: TaskDao,
) : TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override suspend fun getTaskById(taskId: Long): Task? = taskDao.getTaskById(taskId)

    override suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override suspend fun deleteTaskById(taskId: Long) = taskDao.deleteTaskById(taskId)

    override suspend fun getHighestSequence(): Int? = taskDao.getHighestSequence()

    override suspend fun updateTasksSequence(tasks: List<Task>) {
        tasks.forEach { task ->
            taskDao.updateTaskSequence(task.id, task.sequence)
        }
    }

}