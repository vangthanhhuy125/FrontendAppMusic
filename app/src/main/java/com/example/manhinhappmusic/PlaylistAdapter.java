package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{

    private  List<Playlist> playlistList;
    private static OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        if(playlist.getThumnailResID() != 0)
            holder.getImageViewSong().setImageResource(playlist.getThumnailResID());
        holder.getTextViewNumOfSong().setText(String.valueOf(playlist.getSongsList().size()) + " songs");
        holder.getTextViewSongTitle().setText(playlist.getName());

    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public PlaylistAdapter(List<Playlist> playlistList, OnItemClickListener onItemClickListener)
    {
        this.playlistList = playlistList;
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageViewSong;
        private final TextView textViewSongTitle;
        private final TextView textViewNumOfSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onItemClickListener != null)
                    {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
            imageViewSong = itemView.findViewById(R.id.img_song);
            textViewSongTitle = itemView.findViewById(R.id.playlist_title_text);
            textViewNumOfSong = itemView.findViewById(R.id.num_of_song_text);
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

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }
}
