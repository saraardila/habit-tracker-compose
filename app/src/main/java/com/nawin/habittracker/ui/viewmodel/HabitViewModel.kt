package com.nawin.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nawin.habittracker.data.local.entity.HabitEntity
import com.nawin.habittracker.data.local.entity.SubTaskEntity
import com.nawin.habittracker.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    fun updateSubTaskTitle(subTask: SubTaskEntity, newTitle: String) {
        viewModelScope.launch {
            repository.updateSubTaskTitle(subTask.copy(title = newTitle))
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }
    // Insertar predefinidos al inicio
    init {
        viewModelScope.launch {
            repository.insertDefaultIfEmpty()
        }
    }
    fun toggleSubTask(subTask: SubTaskEntity) {
        viewModelScope.launch {
            repository.toggleSubTask(subTask)
        }
    }
}