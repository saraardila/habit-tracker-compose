package com.nawin.habittracker.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nawin.habittracker.data.local.entity.HabitEntity
import com.nawin.habittracker.data.local.entity.SubTaskEntity
import com.nawin.habittracker.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    val habits = repository.habits.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addHabit(title: String) {
        viewModelScope.launch {
            repository.addHabit(title)
        }
    }

    fun createHabit(title: String, subtasks: List<String>) {
        viewModelScope.launch {
            repository.addHabitWithSubTasks(title, subtasks)
        }
    }

    fun updateHabitTitle(habit: HabitEntity, newTitle: String) {
        viewModelScope.launch {
            repository.updateHabitTitle(habit.copy(title = newTitle))
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun toggleSubTask(subTask: SubTaskEntity) {
        viewModelScope.launch {
            repository.toggleSubTask(subTask)
        }
    }

    init {
        viewModelScope.launch {
            repository.insertDefaultIfEmpty()
        }
    }

    fun renameSubTask(subTask: SubTaskEntity, newTitle: String) {
        viewModelScope.launch {
            repository.updateSubTask(subTask.copy(title = newTitle))
        }
    }

    // 🔥 STREAK REAL
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStreakIfCompleted(habit: HabitEntity) {

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        val lastDate = habit.lastCompletedDate?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        var newStreak = habit.currentStreak
        var best = habit.bestStreak

        when (lastDate) {
            yesterday -> newStreak += 1
            today -> return
            else -> newStreak = 1
        }

        if (newStreak > best) best = newStreak

        viewModelScope.launch {
            repository.updateHabitTitle(
                habit.copy(
                    currentStreak = newStreak,
                    bestStreak = best,
                    lastCompletedDate = System.currentTimeMillis()
                )
            )
        }
    }

    // 🎀 BADGES
    fun getBadge(streak: Int): String {
        return when {
            streak >= 30 -> "🌸 30 Day Master"
            streak >= 14 -> "🔥 2 Week Warrior"
            streak >= 7 -> "✨ 7 Day Starter"
            else -> ""
        }
    }
}