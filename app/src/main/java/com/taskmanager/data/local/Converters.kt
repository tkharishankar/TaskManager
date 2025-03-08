package com.taskmanager.data.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Author: Hari K
 * Date: 04/03/2025.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}