package com.homeworkshop.notemvvm

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
class Note(var title: String, var description: String, var priority: Int) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    override fun toString(): String {
        return "Note{id= $id, title=$title  , description=$description  , priority=$priority }"
    }
}