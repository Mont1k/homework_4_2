package com.example.notes.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notes.data.model.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteModel: Note)

    @Query("SELECT * FROM noteModel")
    fun getAll(): LiveData<List<Note>>

    @Delete
    fun deleteNote(noteModel: Note)

    @Update
    fun updateNote(noteModel: Note)

    @Query("SELECT * FROM noteModel WHERE id = :id")
    fun getNoteById(id: Int) : Note?
}