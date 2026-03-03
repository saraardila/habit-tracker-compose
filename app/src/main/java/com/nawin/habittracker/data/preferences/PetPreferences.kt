package com.nawin.habittracker.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pet_prefs")

@Singleton
class PetPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val ACTIVE_PET_KEY = intPreferencesKey("active_pet")
        val UNLOCKED_PETS_KEY = intPreferencesKey("unlocked_pets_mask")
    }

    val activePetIndex: Flow<Int> = context.dataStore.data
        .map { it[ACTIVE_PET_KEY] ?: 0 }

    val unlockedPetsMask: Flow<Int> = context.dataStore.data
        .map { it[UNLOCKED_PETS_KEY] ?: 1 } // primera mascota siempre desbloqueada

    suspend fun setActivePet(index: Int) {
        context.dataStore.edit { it[ACTIVE_PET_KEY] = index }
    }

    suspend fun unlockPet(index: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[UNLOCKED_PETS_KEY] ?: 1
            prefs[UNLOCKED_PETS_KEY] = current or (1 shl index)
        }
    }
}