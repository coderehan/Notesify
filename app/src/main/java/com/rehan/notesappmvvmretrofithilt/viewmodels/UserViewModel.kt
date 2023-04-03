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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel needs repository object as per the MVVM pattern
// @Inject constructor is used inorder to add the dependency of UserRepository in this viewmodel
@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData get() = userRepository.userResponseLiveData

    // We will write some functions in viewmodel and will call those functions in our fragment
    // We will call this method when user clicks on SignUp button
    fun registerUser(userRequest: UserRequest) {
        // Executing this code in background thread of coroutines
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.registerUser(userRequest)
        }
    }

    // We will call this method when user clicks on SignIn button
    fun loginUser(userRequest: UserRequest) {
        // Executing this code in background thread of coroutines
        viewModelScope.launch(Dispatchers.IO) {
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