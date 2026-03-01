package com.nawin.habittracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawin.habittracker.data.local.Habit
import com.nawin.habittracker.data.local.HabitDatabase
import com.nawin.habittracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository

    val habits: StateFlow<List<Habit>>

    init {
        val dao = HabitDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(dao)

        habits = repository.habits.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun addHabit(name: String) {
        viewModelScope.launch {
            repository.addHabit(name)
        }
    }

    fun toggleHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabit(
                habit.copy(isCompletedToday = !habit.isCompletedToday)
            )
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }
}