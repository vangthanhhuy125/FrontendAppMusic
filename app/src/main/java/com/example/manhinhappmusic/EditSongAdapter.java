package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EditSongAdapter extends RecyclerView.Adapter<EditSongAdapter.SongViewHolder> {

    private List<Song> songList;
    private OnSongDeleteListener deleteListener;

    public interface OnSongDeleteListener {
        void onSongDelete(Song song, int position);
    }

    public EditSongAdapter(List<Song> songList, OnSongDeleteListener deleteListener) {
        this.songList = songList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delete_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);

        holder.txtTitle.setText(song.getTitle());
        holder.txtArtist.setText(song.getArtistId());

        if (song.getCoverImageResID() != 0) {
            holder.imageView.setImageResource(song.getCoverImageResID());
        } else {
            holder.imageView.setImageResource(R.drawable.white_baseline_music_note_24);
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onSongDelete(song, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList != null ? songList.size() : 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTitle, txtArtist;
        ImageButton deleteButton;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_Song);
            txtTitle = itemView.findViewById(R.id.txt_Song_Title);
            txtArtist = itemView.findViewById(R.id.txt_Artist);
            deleteButton = itemView.findViewById(R.id.delete_Song_Button);
        }
    }

    public void removeItem(int position) {
        songList.remove(position);
        notifyItemRemoved(position);
    }
}
