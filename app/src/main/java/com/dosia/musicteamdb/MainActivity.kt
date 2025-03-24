package com.dosia.musicteamdb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var singerNames: MutableList<String>
    private lateinit var adapter: SongAdapter
    private lateinit var songsRecyclerView: RecyclerView
    private lateinit var singerSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        songsRecyclerView = findViewById(R.id.songsRecyclerView)
        singerSpinner = findViewById(R.id.singerSpinner)

        db = FirebaseFirestore.getInstance()
        singerNames = mutableListOf()
        adapter = SongAdapter(emptyList())

        // Setup RecyclerView
        songsRecyclerView.layoutManager = LinearLayoutManager(this)
        songsRecyclerView.adapter = adapter

        loadSingers()
    }

    private fun loadSingers() {
        db.collection("Singers")
            .get()
            .addOnSuccessListener { result ->
                singerNames = result.documents.map { it.id }.toMutableList()
                setupSpinner()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error loading singers: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            singerNames
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        singerSpinner.adapter = spinnerAdapter

        singerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedSinger = singerNames[position]
                loadSongs(selectedSinger)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadSongs(singerName: String) {
        db.collection("Singers")
            .document(singerName)
            .collection("Songs")
            .orderBy("Song", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                Log.d("FIREBASE_DATA", "Raw documents: ${result.documents}")
                val songs = result.documents.map { Song.fromDocument(it) }
                Log.d("FIREBASE_DATA", "Parsed songs: $songs")
                adapter.updateSongs(songs)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error loading songs: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}