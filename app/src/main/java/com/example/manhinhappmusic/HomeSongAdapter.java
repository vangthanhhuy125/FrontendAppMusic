package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class HomeSongAdapter extends RecyclerView.Adapter<HomeSongAdapter.ViewHolder> {

    private List<Song> songList;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;

    public HomeSongAdapter(List<Song> songList, OnItemClickListener onItemClickListener)
    {
        this.songList = songList;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_song_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(song.getCoverImageResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getSongsCoverImage());
        holder.getSongsNameText().setText(song.getTitle());
        holder.getArtistsNameText().setText(song.getArtistId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView songsCoverImage;
        private final TextView songsNameText;
        private final TextView artistsNameText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songsCoverImage = itemView.findViewById(R.id.songs_cover_image);
            songsNameText = itemView.findViewById(R.id.songs_name_text);
            artistsNameText = itemView.findViewById(R.id.artists_name_text);

        }


        public ImageView getSongsCoverImage() {
            return songsCoverImage;
        }

        public TextView getArtistsNameText() {
            return artistsNameText;
        }

        public TextView getSongsNameText() {
            return songsNameText;
        }
    }
}
