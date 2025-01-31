package com.example.notes.`interface`

import com.example.notes.data.model.Note

interface OnClickItem {
    fun onLongClick(noteModel: Note)

    fun onClick(noteModel: Note)
}