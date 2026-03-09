package com.nawin.habittracker.data.repository

import com.nawin.habittracker.data.local.dao.DiaryDao
import com.nawin.habittracker.data.local.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val dao: DiaryDao
) {
    val entries: Flow<List<DiaryEntity>> = dao.getAllEntries()

    suspend fun addEntry(text: String, emoji: String) {
        dao.insertEntry(
            DiaryEntity(
                text = text,
                emoji = emoji,
                date = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())
            )
        )
    }

    suspend fun deleteEntry(entry: DiaryEntity) {
        dao.deleteEntry(entry)
    }
}