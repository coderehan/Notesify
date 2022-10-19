package com.rehan.notesappmvvmretrofithilt.utils

// This is google recommended pattern to deal with any API response
sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data)      // Since we have passed 1 parameter to Success class, we will pass 1 parameter to NetworkResult class also
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message) // Since we have passed 2 parameter to Error class, we will pass 2 parameter to NetworkResult class also
    class Loading<T> : NetworkResult<T>()                   // Since we have passed no parameter to Loading class, we will pass no parameter to NetworkResult class

}
