package com.homeworkshop.notemvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository
) : ViewModel() {

    fun getAllNotes () = repository.allNotes()

    fun insert(note: Note) = CoroutineScope(Dispatchers.Default).launch {
        repository.insert(note)
    }

    fun update(note: Note) = CoroutineScope(Dispatchers.Default).launch {
        repository.update(note)
    }

    fun delete(note: Note) = CoroutineScope(Dispatchers.Default).launch {
        repository.delete(note)
    }

    fun deleteAllNotes() = CoroutineScope(Dispatchers.Default).launch {
        repository.deleteAllNotes()
    }

}
