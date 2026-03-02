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
    suspend fun addHabitWithSubTasks(title: String, subtasks: List<String>) {
        val habitId = dao.insertHabit(HabitEntity(title = title)).toInt()
        val entities = subtasks.map { SubTaskEntity(habitId = habitId, title = it) }
        dao.insertSubTasks(entities)
    }

    suspend fun updateHabitTitle(habit: HabitEntity) {
        dao.updateHabit(habit)
    }

    suspend fun updateSubTaskTitle(subTask: SubTaskEntity) {
        dao.updateSubTask(subTask)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        dao.deleteHabit(habit)
    }

    suspend fun insertDefaultHabitsIfEmpty() {
        // se llama al init si no hay hábitos
    }
    suspend fun insertDefaultIfEmpty() {
//        val current = dao.getHabitsOnce() // necesitas añadir esta query
//        if (current.isEmpty()) {
//            val habitId = dao.insertHabit(HabitEntity(title = "Morning Routine")).toInt()
//            dao.insertSubTasks(listOf(
//                SubTaskEntity(habitId = habitId, title = "Drink water"),
//                SubTaskEntity(habitId = habitId, title = "Meditate 5 min"),
//                SubTaskEntity(habitId = habitId, title = "Stretch")
//            ))
//        }
    }
    suspend fun toggleSubTask(subTask: SubTaskEntity) {
        dao.updateSubTask(subTask.copy(isDone = !subTask.isDone))
    }
}