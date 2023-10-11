package com.rehan.notesappmvvmretrofithilt.api

import com.rehan.notesappmvvmretrofithilt.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// What does Interceptor do?
// Before sending request to API, interceptor adds header basically adding token to our API request and then request goes to API and then API gives response for every user as per there unique token. Data is shown as per user token.
// This header is the authorisation header which has token inside.
// Headers that need to be added to every request can be specified using an OkHttp interceptor. We have already added dependency for OkHttp interceptor in build.gradle

// We have to add this class in retrofit module.

// This class is the best way to add headers for all our API requests automatically.
// We don't need to add token to all our API request individually because it will take more times if we have lots of API request in our API interface.
// This class will take care of everything automatically for adding headers to all API request.
// We need to extend this class from inbuilt Interceptor class of Okhttp library.

// The purpose of this class is to add token as header for all notes api request automatically instead of adding them one by one in notes api interface. Because every user will have different notes and these notes are associated with token. So we need token which has all the notes of user inside it.
class AuthInterceptor @Inject constructor() : Interceptor {     // This Interceptor class is derived from OkHttp

    @Inject
    lateinit var tokenManager: TokenManager

    // This intercept method will observe our request to API and do something like adding header to request
    // Here we are going to add header i.e. token to our API request
    // Before sending request to API, first we will get that request and will add header to it and then we will tell request go and hit API

    override fun intercept(chain: Interceptor.Chain): Response {
        // Creating new request object so that we can add header to it
        val request = chain.request().newBuilder()

        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")     // "Bearer" keyword is the api requirement actually and we have to pass like this only whenever we want to add header i.e token to it

        return chain.proceed(request.build())
    }
}