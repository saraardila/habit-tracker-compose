package com.nawin.habittracker.di

import android.content.Context
import androidx.room.Room
import com.nawin.habittracker.data.local.dao.HabitDao
import com.nawin.habittracker.data.local.dao.HabitDatabase
import com.nawin.habittracker.data.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): HabitDatabase {
        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            "habit_db"
        ).build()
    }

    @Provides
    fun provideHabitDao(
        database: HabitDatabase
    ): HabitDao = database.habitDao()

    @Provides
    @Singleton
    fun provideRepository(
        dao: HabitDao
    ): HabitRepository {
        return HabitRepository(dao)
    }
}