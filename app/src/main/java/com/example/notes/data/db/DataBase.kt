package com.example.notes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.data.db.dao.NoteDao
import com.example.notes.data.model.Note

@Database(entities = [Note::class], version = 2)
abstract class DataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}