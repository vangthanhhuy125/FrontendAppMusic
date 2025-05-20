package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private final List<Playlist> songItems;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = songItems.get(position);
        holder.getImageViewSong().setImageResource(R.drawable.exampleavatar);
        holder.getTextViewNumOfSong().setText(String.valueOf(playlist.getSongs().size()));
        holder.getTextViewSongTitle().setText(playlist.getDescription());

    }

    @Override
    public int getItemCount() {
        return songItems.size();
    }

    public LibraryAdapter(List<Playlist> songItems)
    {
        this.songItems = songItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageViewSong;
        private final TextView textViewSongTitle;
        private final TextView textViewNumOfSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSong = itemView.findViewById(R.id.img_song);
            textViewSongTitle = itemView.findViewById(R.id.txt_song_title);
            textViewNumOfSong = itemView.findViewById(R.id.txt_num_of_song);
        }

        public TextView getTextViewSongTitle() {
            return textViewSongTitle;
        }

        public ImageView getImageViewSong() {
            return imageViewSong;
        }

        public TextView getTextViewNumOfSong() {
            return textViewNumOfSong;
        }
    }
}
