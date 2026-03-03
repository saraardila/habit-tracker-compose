package com.nawin.habittracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nawin.habittracker.data.preferences.ALL_PETS
import com.nawin.habittracker.data.preferences.PetConfig
import com.nawin.habittracker.data.preferences.PetPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petPreferences: PetPreferences
) : ViewModel() {

    val activePetIndex: StateFlow<Int> = petPreferences.activePetIndex
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val unlockedMask: StateFlow<Int> = petPreferences.unlockedPetsMask
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val activePet: StateFlow<PetConfig> = activePetIndex
        .map { ALL_PETS[it.coerceIn(0, ALL_PETS.size - 1)] }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ALL_PETS[0])

    fun isPetUnlocked(index: Int, mask: Int): Boolean {
        return (mask and (1 shl index)) != 0
    }

    fun setActivePet(index: Int) {
        viewModelScope.launch {
            petPreferences.setActivePet(index)
        }
    }

    fun unlockPetIfEligible(currentStreak: Int) {
        viewModelScope.launch {
            ALL_PETS.forEach { pet ->
                if (currentStreak >= pet.requiredStreak) {
                    petPreferences.unlockPet(pet.index)
                }
            }
        }
    }
}