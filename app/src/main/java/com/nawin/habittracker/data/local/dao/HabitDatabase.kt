package com.nawin.habittracker.data.local.dao


import androidx.room.Database
import androidx.room.RoomDatabase
import com.nawin.habittracker.data.local.entity.HabitEntity
import com.nawin.habittracker.data.local.entity.SubTaskEntity

@Database(
    entities = [HabitEntity::class, SubTaskEntity::class],
    version = 1
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}