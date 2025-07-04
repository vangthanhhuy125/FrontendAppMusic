package com.example.manhinhappmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.model.Song;

import java.util.List;

public class AdminSongAdapter extends RecyclerView.Adapter<AdminSongAdapter.ViewHolder> {
    public interface OnRemoveSongClickListener {
        void onRemove(String songId);
    }

    private List<Song> songs;
    private OnRemoveSongClickListener listener;

    public AdminSongAdapter(List<Song> songs, OnRemoveSongClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        String imageUrl = song.getCoverImageUrl();
        android.util.Log.d("AdminSongAdapter", "Image URL for song: " + song.getTitle() + " => " + imageUrl);
        Glide.with(holder.image.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.white_baseline_music_note_24) // thêm placeholder nếu cần
                .into(holder.image);
        holder.deleteButton.setOnClickListener(v -> listener.onRemove(song.getId()));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongList(List<Song> songs) {
        this.songs = songs;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView image;
        ImageButton deleteButton;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_Song_Title);
            artist = view.findViewById(R.id.txt_Artist);
            image = view.findViewById(R.id.imageView_Song);
            deleteButton = view.findViewById(R.id.delete_Song_Button);
        }
    }
}
