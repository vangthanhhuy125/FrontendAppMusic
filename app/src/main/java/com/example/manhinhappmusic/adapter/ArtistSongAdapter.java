package com.example.manhinhappmusic.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiService;

import java.util.List;

import lombok.Getter;

@Getter
public class ArtistSongAdapter extends RecyclerView.Adapter<ArtistSongAdapter.ViewHolder> {
    private  List<Song> songList;
    private static OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position, Song song);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.getTextViewSongTitle().setText(song.getTitle());
        if(song.getCoverImageUrl()!= null && !song.getCoverImageUrl().isEmpty())
            Glide.with(holder.itemView.getContext())
                    .load(ApiService.BASE_URL + song.getCoverImageUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(holder.getImageViewThumbnail());
        else
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.music_default_cover)
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(holder.getImageViewThumbnail());
        holder.itemView.setOnClickListener(v -> {
            if(itemClickListener != null)
            {
                itemClickListener.onItemClick(holder.getAdapterPosition(), song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (songList != null) ? songList.size() : 0;
    }

    public ArtistSongAdapter(List<Song> songList, OnItemClickListener itemClickListener)
    {
        this.songList = songList;
        this.itemClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewSongTitle;
        private final ImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSongTitle = itemView.findViewById(R.id.text_name_song);
            imageViewThumbnail = itemView.findViewById(R.id.img_artist_song);
        }


        public TextView getTextViewSongTitle() {
            return textViewSongTitle;
        }


        public ImageView getImageViewThumbnail() {
            return imageViewThumbnail;
        }
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
