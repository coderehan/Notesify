package com.rehan.notesappmvvmretrofithilt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rehan.notesappmvvmretrofithilt.api.NotesAPI
import com.rehan.notesappmvvmretrofithilt.models.notes.NotesRequest
import com.rehan.notesappmvvmretrofithilt.models.notes.NotesResponse
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

// Repository will talk with API interface as per MVVM pattern
// @Inject constructor is used inorder to add the dependency of NotesAPI in this repository
class NotesRepository @Inject constructor(private val notesAPI: NotesAPI) {

    private val _notesMutableLiveData = MutableLiveData<NetworkResult<List<NotesResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NotesResponse>>>
        get() = _notesMutableLiveData

    // Create, update and delete functions returns status whether notes is successfully created or not, deleted or not, updated or not.
    // So we have to create one more mutable live data to return their status.
    private val _statusMutableLiveData = MutableLiveData<NetworkResult<String>>()      // The reason why we use string here is when request fails, we have to show error message which is of string type
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusMutableLiveData

    // Here we are showing data which is of list type. So we are using MutableLiveData<NetworkResult<List<NotesResponse>>>()
    suspend fun getNotes() {
        _notesMutableLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.getNotes()

        if (response.isSuccessful && response.body() != null) {
            // In retrofit when our request is successful, json data in response.body() retrofit will parse that json data into java/kotlin objects and we get corresponding data. This happens only in successful state of retrofit, not in error state of retrofit
            _notesMutableLiveData.postValue(NetworkResult.Success(response.body()!!))     // Here we will set our API response in live data object
        } else if (response.errorBody() != null) {
            // In retrofit, there is only json data in response.errorBody() but this json data will not parse into java/kotlin objects. So we have to create java/kotlin objects on owr own
            // In android, we have inbuilt class JsonObject which will parse the json data. We will make use of this JsonObject method to parse the json data of error body
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _notesMutableLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))      // "message" is the key name in API in retrofit error body like message = User already exists
        } else {
            _notesMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNotes(notesRequest: NotesRequest) {
        _statusMutableLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.createNotes(notesRequest)   // Raising API request
        handleResponse(response, "Notes created successfully")
    }

    suspend fun deleteNotes(noteId: String) {
        _statusMutableLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.deleteNotes(noteId)         // Raising API request
        handleResponse(response, "Notes deleted successfully")
    }

    suspend fun updateNotes(noteId: String, notesRequest: NotesRequest) {
        _statusMutableLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.updateNotes(noteId, notesRequest)       // Raising API request
        handleResponse(response, "Notes updated successfully")
    }

    private fun handleResponse(response: Response<NotesResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusMutableLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusMutableLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}