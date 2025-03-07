package com.dosia.musicteamdb

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.PropertyName

data class Song(
    val songKey: String = "",
    val song: String = "",
    val version: String = ""
) {
    constructor() : this("", "", "")  // Required for Firestore

    companion object {
        fun fromDocument(document: DocumentSnapshot): Song {
            return Song(
                songKey = document.getString("Scale") ?: "",
                song = document.getString("Song") ?: "",
                version = document.getString("Version") ?: ""
            )
        }
    }
}