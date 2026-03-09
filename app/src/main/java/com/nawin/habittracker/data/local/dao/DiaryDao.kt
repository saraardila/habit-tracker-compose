package com.nawin.habittracker.data.local.dao

import androidx.room.*
import com.nawin.habittracker.data.local.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<DiaryEntity>>

    @Insert
    suspend fun insertEntry(entry: DiaryEntity)

    @Delete
    suspend fun deleteEntry(entry: DiaryEntity)
}