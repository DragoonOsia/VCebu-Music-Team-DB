package com.dosia.musicteamdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(private var songs: List<Song>) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSong: TextView = view.findViewById(R.id.tvSong)
        val tvVersion: TextView = view.findViewById(R.id.tvVersion)
        val tvSongKey: TextView = view.findViewById(R.id.tvSongKey)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.tvSong.text = song.song.takeIf { it.isNotEmpty() } ?: "No Song"
        holder.tvVersion.text = song.version.takeIf { it.isNotEmpty() } ?: "No Version"
        holder.tvSongKey.text = song.songKey.takeIf { it.isNotEmpty() } ?: "No Scale"
    }

    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    override fun getItemCount() = songs.size
}