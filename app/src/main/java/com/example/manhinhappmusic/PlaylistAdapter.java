package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{

    private final List<Song> playlistItems;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = playlistItems.get(position);
        holder.getTextViewArtist().setText(song.getArtistId());
        holder.getTextViewSongTitle().setText(song.getTitle());
        holder.getImageViewMore().setImageResource(R.drawable.white_baseline_add_circle_outline_24);
        holder.getImageViewThumbnail().setImageResource(R.drawable.exampleavatar);

    }

    @Override
    public int getItemCount() {
        return (playlistItems != null) ? playlistItems.size() : 0;
    }

    public PlaylistAdapter(List<Song> playlistItems)
    {
        this.playlistItems = playlistItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewArtist;
        private final TextView textViewSongTitle;
        private final ImageView imageViewMore;
        private final ShapeableImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewArtist = itemView.findViewById(R.id.txtArtist);
            textViewSongTitle = itemView.findViewById(R.id.txtSongTitle);
            imageViewMore = itemView.findViewById(R.id.imgMore);
            imageViewThumbnail = itemView.findViewById(R.id.imgThumbnail);
        }

        public TextView getTextViewArtist() {
            return textViewArtist;
        }

        public TextView getTextViewSongTitle() {
            return textViewSongTitle;
        }

        public ImageView getImageViewMore() {
            return imageViewMore;
        }

        public ShapeableImageView getImageViewThumbnail() {
            return imageViewThumbnail;
        }
    }
}
