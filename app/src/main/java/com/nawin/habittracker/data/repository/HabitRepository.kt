package com.nawin.habittracker.data.repository

import com.nawin.habittracker.data.local.Habit
import com.nawin.habittracker.data.local.HabitDao

class HabitRepository(
    private val dao: HabitDao
) {

    val habits = dao.getHabits()

    suspend fun addHabit(name: String) {
        dao.insertHabit(Habit(name = name))
    }

    suspend fun updateHabit(habit: Habit) {
        dao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        dao.deleteHabit(habit)
    }
}