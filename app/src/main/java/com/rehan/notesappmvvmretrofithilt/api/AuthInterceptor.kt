package com.rehan.notesappmvvmretrofithilt.api

import com.rehan.notesappmvvmretrofithilt.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// What does Interceptor do?
// Before sending request to API, interceptor adds header to request and then request goes to API

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    // This intercept method will observe our request to API and do something like adding header to request
    // Here we are going to add header i.e. token to our request
    // Before sending request to API, first we will get that request and will add header to it and then we will tell request go and hit API

    override fun intercept(chain: Interceptor.Chain): Response {
        // Creating new request object so that we can add header to it
        val request = chain.request().newBuilder()

        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")     // "Bearer" is the api requirement and we have to pass like this only whenever we want to add token header to it

        return chain.proceed(request.build())
    }
}