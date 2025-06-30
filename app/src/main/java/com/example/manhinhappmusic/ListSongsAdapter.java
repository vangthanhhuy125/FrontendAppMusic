package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.SongViewHolder> {

    private List<Song> songList;
    private OnSongClickListener listener;

    public interface OnSongClickListener {
        void onEditClick(Song song);
        void onDeleteClick(Song song);
    }

    public ListSongsAdapter(List<Song> songList, OnSongClickListener listener) {
        this.songList = songList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_songs, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        if (songList == null || position >= songList.size()) return;

        Song song = songList.get(position);

        holder.textTitle.setText(song.getTitle() != null ? song.getTitle() : "");
        holder.textArtist.setText(song.getArtistId() != null ? song.getArtistId() : "");

        int imageResId = song.getCoverImageResID();
        if (imageResId != 0) {
            holder.imageThumbnail.setImageResource(imageResId);
        } else {
            holder.imageThumbnail.setImageResource(R.drawable.exampleavatar);
        }

        holder.iconEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(song);
            }
        });

        holder.iconDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList != null ? songList.size() : 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumbnail, iconEdit, iconDelete;
        TextView textTitle, textArtist, textViews;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imageThumbnail = itemView.findViewById(R.id.image_thumbnail);
            textTitle = itemView.findViewById(R.id.text_title);
            textArtist = itemView.findViewById(R.id.text_artist);
            textViews = itemView.findViewById(R.id.text_views);
            iconEdit = itemView.findViewById(R.id.icon_edit);
            iconDelete = itemView.findViewById(R.id.delete_icon);
        }
    }
}
