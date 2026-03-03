package com.nawin.habittracker.di

import android.content.Context
import com.nawin.habittracker.data.local.dao.DiaryDao
import com.nawin.habittracker.data.local.dao.HabitDao
import com.nawin.habittracker.data.local.dao.HabitDatabase
import com.nawin.habittracker.data.preferences.PetPreferences
import com.nawin.habittracker.data.repository.DiaryRepository
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
    ): HabitDatabase = HabitDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideHabitDao(db: HabitDatabase): HabitDao = db.habitDao()

    @Provides
    @Singleton
    fun provideDiaryDao(db: HabitDatabase): DiaryDao = db.diaryDao()

    @Provides
    @Singleton
    fun provideHabitRepository(dao: HabitDao): HabitRepository = HabitRepository(dao)

    @Provides
    @Singleton
    fun provideDiaryRepository(dao: DiaryDao): DiaryRepository = DiaryRepository(dao)

    @Provides
    @Singleton
    fun providePetPreferences(
        @ApplicationContext context: Context
    ): PetPreferences = PetPreferences(context)
}