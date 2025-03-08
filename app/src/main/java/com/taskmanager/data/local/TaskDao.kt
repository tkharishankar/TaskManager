package com.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)

    @Query("SELECT MAX(sequence) FROM tasks")
    suspend fun getHighestSequence(): Int?

    @Query("UPDATE tasks SET sequence = :sequence WHERE id = :taskId")
    suspend fun updateTaskSequence(taskId: Long, sequence: Int)
}