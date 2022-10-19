package com.rehan.notesappmvvmretrofithilt.api

import com.rehan.notesappmvvmretrofithilt.models.NotesRequest
import com.rehan.notesappmvvmretrofithilt.models.NotesResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("/note")
    suspend fun getNotes(): Response<List<NotesResponse>>

    @POST("/note")
    suspend fun createNotes(@Body notesRequest: NotesRequest): Response<NotesResponse>

    @PUT("/note/{noteId}")      // {} denotes dynamic which means user can update any notes from the list. when we pass parameter in {} we have to use @Path annotation
    suspend fun updateNotes(@Path("noteId") noteId: String, @Body notesRequest: NotesRequest) : Response<NotesResponse>

    @DELETE("/note/{noteId}")   // When we pass anything in {} we have to use @Path annotation
    suspend fun deleteNotes(@Path("noteId") noteId: String) : Response<NotesResponse>


}