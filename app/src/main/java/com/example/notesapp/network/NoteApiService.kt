package com.example.notesapp.network
import com.example.notesapp.model.Note
import retrofit2.Call
import retrofit2.http.*

interface NoteApiService {
    @GET("api/v1/note/all")
    fun getAllNotes(): Call<List<Note>>

    @GET("api/v1/note/{id}")
    fun getNoteById(@Path("id") id: Long): Call<Note>

    @GET("api/v1/note/title/{title}")
    fun getNoteByTitle(@Path("title") title: String): Call<Note>

    @POST("api/v1/note/save")
    fun createNote(@Body note: Note): Call<Note>

    @PUT("api/v1/note/update/{id}")
    fun updateNote(@Path("id") id: Long, @Body note: Note): Call<Note>

    @DELETE("api/v1/note/{id}")
    fun deleteNote(@Path("id") id: Long): Call<Void>
}
