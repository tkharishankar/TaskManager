package com.taskmanager.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val dueDate: Date?,
    @ColumnInfo(name = "priority_value")
    val priorityValue: Int,
    val isCompleted: Boolean = false,
    val sequence: Int,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
) {
    @Ignore
    val priority: Priority = Priority.fromValue(priorityValue)
}

enum class Priority(val value: Int) {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    companion object {
        fun fromValue(value: Int) = entries.firstOrNull { it.value == value } ?: LOW
    }
}