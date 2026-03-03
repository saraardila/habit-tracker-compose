package com.nawin.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nawin.habittracker.data.local.entity.DiaryEntity
import com.nawin.habittracker.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {

    val entries: StateFlow<List<DiaryEntity>> = repository.entries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(text: String, emoji: String) {
        viewModelScope.launch {
            repository.addEntry(text, emoji)
        }
    }

    fun deleteEntry(entry: DiaryEntity) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }
}