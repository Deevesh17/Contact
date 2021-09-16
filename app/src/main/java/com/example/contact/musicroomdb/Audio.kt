package com.example.contact.musicroomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio")
class Audio(
    @PrimaryKey(autoGenerate = true)
    private var musicID: Int
) {
    private var music : String
        get() {
            return music
        }
        set(value) {
            this.music = value
        }
}