package com.nawin.habittracker.data.repository

import com.nawin.habittracker.data.local.dao.HabitDao
import com.nawin.habittracker.data.local.entity.HabitEntity
import com.nawin.habittracker.data.local.entity.SubTaskEntity

class HabitRepository(
    private val dao: HabitDao,
) {

    val habits = dao.getHabits()

    suspend fun addHabit(title: String) {
        val habitId = dao.insertHabit(HabitEntity(title = title)).toInt()

        val defaultSubtasks = listOf(
            SubTaskEntity(habitId = habitId, title = "Step 1"),
            SubTaskEntity(habitId = habitId, title = "Step 2"),
            SubTaskEntity(habitId = habitId, title = "Step 3")
        )

        dao.insertSubTasks(defaultSubtasks)
    }

    suspend fun toggleSubTask(subTask: SubTaskEntity) {
        dao.updateSubTask(subTask.copy(isDone = !subTask.isDone))
    }
}