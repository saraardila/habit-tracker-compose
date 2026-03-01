package com.nawin.habittracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nawin.habittracker.data.local.entity.HabitEntity
import com.nawin.habittracker.data.local.entity.HabitWithSubTasks
import com.nawin.habittracker.data.local.entity.SubTaskEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitDao {

    @Transaction
    @Query("SELECT * FROM habits")
    fun getHabits(): Flow<List<HabitWithSubTasks>>

    @Insert
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert
    suspend fun insertSubTasks(subtasks: List<SubTaskEntity>)

    @Update
    suspend fun updateSubTask(subTask: SubTaskEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}