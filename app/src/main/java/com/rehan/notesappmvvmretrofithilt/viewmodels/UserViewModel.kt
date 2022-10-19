package com.rehan.notesappmvvmretrofithilt.viewmodels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rehan.notesappmvvmretrofithilt.models.UserRequest
import com.rehan.notesappmvvmretrofithilt.models.UserResponse
import com.rehan.notesappmvvmretrofithilt.repositories.UserRepository
import com.rehan.notesappmvvmretrofithilt.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    // This validate users input function we can keep any where. We don't need to specify here in view model only.
    fun validateCredentials(username: String, emailAddress: String, password: String, isLogin: Boolean) : Pair<Boolean, String>{          // Pair  is an inbuilt class in kotlin which takes two values as parameters
        var result = Pair(true, "")         // By default, we will keep boolean value true and string as empty

        if(!isLogin && TextUtils.isEmpty(username) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair(false, "Please provide valid Email ID")
        }else if(password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
}