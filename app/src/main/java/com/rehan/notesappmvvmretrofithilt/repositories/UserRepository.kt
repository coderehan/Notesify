package com.rehan.notesappmvvmretrofithilt.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rehan.notesappmvvmretrofithilt.api.UserAPI
import com.rehan.notesappmvvmretrofithilt.models.UserRequest
import com.rehan.notesappmvvmretrofithilt.models.UserResponse
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

// Repository will talk with API interface as per MVVM pattern
// @Inject constructor is used inorder to add the dependency of UserAPI in this repository
class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    // Generally we create 2 live data properties (1) private MutableLiveData to set our data in repository itself (2) public LiveData to access it publicly
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData       // We will pass the mutable livedata object to public livedata

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }


    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signin(userRequest)
        handleResponse(response)
    }

    // Handling response coming from API
    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            // In retrofit when our request is successful, json data in response.body() retrofit will parse that json data into java/kotlin objects and we get corresponding data. This happens only in successful state of retrofit, not in error state of retrofit
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))   // Here we will set our API response in live data object
        } else if (response.errorBody() != null) {
            // In retrofit, there is only json data in response.errorBody() but this json data will not parse into java/kotlin objects. So we have to create java/kotlin objects on owr own
            // In android, we have inbuilt class JsonObject which will parse the json data. We will make use of this JsonObject method to parse the json data of error body
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))      // "message" is the key name in API in retrofit error body like message = User already exists
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}