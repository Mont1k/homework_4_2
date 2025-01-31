package com.example.notes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noteModel")
data class Note(
    val title: String,
    val description: String,
    val data: String,
    val color: Int
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(): this("", "", "", 0)
}