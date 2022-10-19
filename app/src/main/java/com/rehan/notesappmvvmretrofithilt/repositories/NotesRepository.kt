package com.rehan.notesappmvvmretrofithilt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rehan.notesappmvvmretrofithilt.api.NotesAPI
import com.rehan.notesappmvvmretrofithilt.models.NotesRequest
import com.rehan.notesappmvvmretrofithilt.models.NotesResponse
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesAPI: NotesAPI) {

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NotesResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NotesResponse>>>
        get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()      // The reason why we use string here is when request fails, we have to show error message which is of string type
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    // Here we are showing data which is of list type. So we are using MutableLiveData<NetworkResult<List<NotesResponse>>>()
    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.getNotes()

        if (response.isSuccessful && response.body() != null) {
            // In retrofit when our request is successful, json data in response.body() retrofit will parse that json data into java/kotlin objects and we get corresponding data. This happens only in successful state of retrofit, not in error state of retrofit
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))     // Here we will set our API response in live data object
        } else if (response.errorBody() != null) {
            // In retrofit, there is only json data in response.errorBody() but this json data will not parse into java/kotlin objects. So we have to create java/kotlin objects on owr own
            // In android, we have inbuilt class JsonObject which will parse the json data. We will make use of this JsonObject method to parse the json data of error body
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))      // "message" is the key name in API in retrofit error body like message = User already exists
        } else {
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNotes(notesRequest: NotesRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.createNotes(notesRequest)
        handleResponse(response, "Notes created successfully")
    }

    suspend fun deleteNotes(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.deleteNotes(noteId)
        handleResponse(response, "Notes deleted successfully")
    }

    suspend fun updateNotes(noteId: String, notesRequest: NotesRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.updateNotes(noteId, notesRequest)
        handleResponse(response, "Notes updated successfully")
    }

    private fun handleResponse(response: Response<NotesResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}