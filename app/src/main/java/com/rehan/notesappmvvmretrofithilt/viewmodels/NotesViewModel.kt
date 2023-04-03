package com.rehan.notesappmvvmretrofithilt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.notesappmvvmretrofithilt.models.NotesRequest
import com.rehan.notesappmvvmretrofithilt.repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel needs repository object as per the MVVM pattern
// @Inject constructor is used inorder to add the dependency of NotesRepository in this viewmodel
@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {

    val notesLiveData get() = notesRepository.notesLiveData
    val statusLiveData get() = notesRepository.statusLiveData

    fun getNotes() {
        // Executing this code in background thread of coroutines
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.getNotes()      // Inside coroutines, we are calling repository functions
        }
    }

    fun createNotes(notesRequest: NotesRequest) {
        // Executing this code in background thread of coroutines
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.createNotes(notesRequest)       // Inside coroutines, we are calling repository functions
        }
    }

    fun updateNotes(noteId: String, notesRequest: NotesRequest) {
        // Executing this code in background thread of coroutines
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.updateNotes(noteId, notesRequest)       // Inside coroutines, we are calling repository functions
        }
    }

    fun deleteNotes(noteId: String) {
        // Executing this code in background thread of coroutines
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNotes(noteId)             // Inside coroutines, we are calling repository functions
        }
    }

}