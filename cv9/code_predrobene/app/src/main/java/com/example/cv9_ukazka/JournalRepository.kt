package com.example.cv9_ukazka

class JournalRepository(
    private val journalEntryDao: JournalEntryDao
) {
    suspend fun getAllEntries(): List<JournalEntryEntity> {
        return journalEntryDao.getAllEntries()
    }

    suspend fun insertEntry(entry: JournalEntryEntity) {
        journalEntryDao.insertEntry(entry)
    }

    suspend fun deleteEntry(entry: JournalEntryEntity) {
        journalEntryDao.deleteEntry(entry)
    }
}
