package com.rehan.notesappmvvmretrofithilt.api

import com.rehan.notesappmvvmretrofithilt.models.notes.NotesRequest
import com.rehan.notesappmvvmretrofithilt.models.notes.NotesResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    // All these endpoints are authenticated endpoints. We need to pass token in header because every user will be having his own unique id called token.

    @GET("/note")
    suspend fun getNotes(): Response<List<NotesResponse>>

    @POST("/note")
    suspend fun createNotes(@Body notesRequest: NotesRequest): Response<NotesResponse>

    @PUT("/note/{noteId}")      // {} denotes dynamic which means user can update any notes from the list. when we pass parameter inside {} we have to use @Path annotation
    suspend fun updateNotes(@Path("noteId") noteId: String, @Body notesRequest: NotesRequest) : Response<NotesResponse>

    @DELETE("/note/{noteId}")   // When we pass anything inside {} we have to use @Path annotation
    suspend fun deleteNotes(@Path("noteId") noteId: String) : Response<NotesResponse>


}