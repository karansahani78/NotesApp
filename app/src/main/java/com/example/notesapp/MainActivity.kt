package com.example.notesapp

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.adapter.NoteAdapter
import com.example.notesapp.model.Note
import com.example.notesapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSearchById: Button
    private lateinit var btnSearchByTitle: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private var selectedNoteId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnAdd = findViewById(R.id.btnAdd)
        btnSearchById = findViewById(R.id.btnSearchById)
        btnSearchByTitle = findViewById(R.id.btnSearchByTitle)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        adapter = NoteAdapter(listOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchAllNotes()

        // Add Note
        btnAdd.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(title = title, content = content)
                RetrofitClient.api.createNote(note).enqueue(object : Callback<Note> {
                    override fun onResponse(call: Call<Note>, response: Response<Note>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Note added", Toast.LENGTH_SHORT).show()
                            fetchAllNotes()
                            clearFields()
                        }
                    }

                    override fun onFailure(call: Call<Note>, t: Throwable) {
                        Log.e("API", "Add note failed: ${t.message}")
                    }
                })
            } else {
                Toast.makeText(this, "Please enter title and content", Toast.LENGTH_SHORT).show()
            }
        }

        // Search by ID
        btnSearchById.setOnClickListener {
            val idStr = etTitle.text.toString()
            if (idStr.isNotEmpty()) {
                val id = idStr.toLongOrNull()
                if (id != null) {
                    RetrofitClient.api.getNoteById(id).enqueue(object : Callback<Note> {
                        override fun onResponse(call: Call<Note>, response: Response<Note>) {
                            if (response.isSuccessful) {
                                adapter.updateData(listOf(response.body()!!))
                                selectedNoteId = response.body()?.id
                            }
                        }

                        override fun onFailure(call: Call<Note>, t: Throwable) {
                            Log.e("API", "Search by ID failed: ${t.message}")
                        }
                    })
                }
            }
        }

        // Search by Title
        btnSearchByTitle.setOnClickListener {
            val title = etTitle.text.toString()
            if (title.isNotEmpty()) {
                val encodedTitle = URLEncoder.encode(title, "UTF-8")
                RetrofitClient.api.getNoteByTitle(encodedTitle).enqueue(object : Callback<Note> {
                    override fun onResponse(call: Call<Note>, response: Response<Note>) {
                        if (response.isSuccessful) {
                            adapter.updateData(listOf(response.body()!!))
                            selectedNoteId = response.body()?.id
                        }
                    }

                    override fun onFailure(call: Call<Note>, t: Throwable) {
                        Log.e("API", "Search by title failed: ${t.message}")
                    }
                })
            }
        }

        // Update Note
        btnUpdate.setOnClickListener {
            val id = selectedNoteId
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (id != null && title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(title = title, content = content)
                RetrofitClient.api.updateNote(id, note).enqueue(object : Callback<Note> {
                    override fun onResponse(call: Call<Note>, response: Response<Note>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Note updated", Toast.LENGTH_SHORT).show()
                            fetchAllNotes()
                            clearFields()
                        }
                    }

                    override fun onFailure(call: Call<Note>, t: Throwable) {
                        Log.e("API", "Update failed: ${t.message}")
                    }
                })
            }
        }

        // Delete Note
        btnDelete.setOnClickListener {
            val id = selectedNoteId
            if (id != null) {
                RetrofitClient.api.deleteNote(id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                            fetchAllNotes()
                            clearFields()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("API", "Delete failed: ${t.message}")
                    }
                })
            }
        }
    }

    private fun fetchAllNotes() {
        RetrofitClient.api.getAllNotes().enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    adapter.updateData(response.body() ?: listOf())
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.e("API", "Fetch all notes failed: ${t.message}")
            }
        })
    }

    private fun clearFields() {
        etTitle.text.clear()
        etContent.text.clear()
        selectedNoteId = null
    }
}
