package com.nawin.habittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastCompletedDate: Long? = null,
    val badgeLevel: Int = 0
)