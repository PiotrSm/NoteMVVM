package com.homeworkshop.notemvvm

import androidx.lifecycle.LiveData

class NoteRepository(private val db: NoteDatabase) {
    fun allNotes(): LiveData<List<Note>> = db.noteDao().allNotes

    suspend fun insert(note: Note) = db.noteDao().insert(note)

    suspend fun update(note: Note) = db.noteDao().update(note)

    suspend fun delete(note: Note) = db.noteDao().delete(note)

    suspend fun deleteAllNotes() = db.noteDao().deleteAllNotes()

}