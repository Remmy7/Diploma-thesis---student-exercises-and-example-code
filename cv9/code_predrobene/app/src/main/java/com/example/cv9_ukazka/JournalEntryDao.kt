package com.example.cv9_ukazka

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JournalEntryDao {

    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    suspend fun getAllEntries(): List<JournalEntryEntity>

    @Insert
    suspend fun insertEntry(entry: JournalEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: JournalEntryEntity)
}
