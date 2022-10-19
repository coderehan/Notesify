package com.rehan.notesappmvvmretrofithilt.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.notesappmvvmretrofithilt.models.NotesRequest
import com.rehan.notesappmvvmretrofithilt.repositories.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {

    val notesLiveData get() = notesRepository.notesLiveData
    val statusLiveData get() = notesRepository.statusLiveData

    fun getNotes() {
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }

    fun createNotes(notesRequest: NotesRequest) {
        viewModelScope.launch {
            notesRepository.createNotes(notesRequest)
        }
    }

    fun updateNotes(noteId: String, notesRequest: NotesRequest) {
        viewModelScope.launch {
            notesRepository.updateNotes(noteId, notesRequest)
        }
    }

    fun deleteNotes(noteId: String) {
        viewModelScope.launch {
            notesRepository.deleteNotes(noteId)
        }
    }

}