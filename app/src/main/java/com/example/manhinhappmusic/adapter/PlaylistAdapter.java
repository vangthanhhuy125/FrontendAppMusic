package com.example.manhinhappmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.model.Playlist;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{

    private  List<Playlist> playlistList;
    private final String token;
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
    public void onBindViewHolder(PlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        String thumbnailUrl = playlist.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            String baseUrl = ApiClient.BASE_URL;
            if (baseUrl.endsWith("/") && thumbnailUrl.startsWith("/")) {
                thumbnailUrl = thumbnailUrl.substring(1); // bỏ dấu / đầu tiên
            }
            String fullUrl = baseUrl + thumbnailUrl;

            Glide.with(holder.itemView.getContext())
                    .load(new GlideUrl(fullUrl, new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build()))
                    .apply(new RequestOptions().transform(
                            new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(holder.getImageViewSong());
        }
        holder.getTextViewNumOfSong().setText(String.valueOf(playlist.getSongsList().size()) + " songs");
        holder.getTextViewSongTitle().setText(playlist.getName());
    }

    @Override
    public int getItemCount() {
        if (playlistList == null) return 0;
        return playlistList.size();
    }

    public PlaylistAdapter(String token, List<Playlist> playlistList, OnItemClickListener onItemClickListener)
    {
        this.playlistList = playlistList;
        this.onItemClickListener = onItemClickListener;
        this.token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageViewSong;
        private final TextView textViewSongTitle;
        private final TextView textViewNumOfSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && onItemClickListener != null)
                {
                    onItemClickListener.onItemClick(position);
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