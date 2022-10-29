package com.rehan.notesappmvvmretrofithilt.api

import com.rehan.notesappmvvmretrofithilt.models.UserRequest
import com.rehan.notesappmvvmretrofithilt.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Response<UserResponse>      // @Body annotation gets input from user

    @POST("/users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Response<UserResponse>
}
