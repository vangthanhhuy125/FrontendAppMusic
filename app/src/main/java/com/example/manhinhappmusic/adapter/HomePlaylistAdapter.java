package com.example.manhinhappmusic.adapter;

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
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class HomePlaylistAdapter extends RecyclerView.Adapter<HomePlaylistAdapter.ViewHolder> {

    private List<Playlist> playlistList = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener onItemClickListener;

    public HomePlaylistAdapter(List<Playlist> playlistList, OnItemClickListener onItemClickListener) {
        if (playlistList != null)
            this.playlistList = playlistList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<Playlist> newPlaylistList) {
        this.playlistList = newPlaylistList != null ? newPlaylistList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_playlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(playlist.getThumbnailUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getPlaylistsCoverImage());

        holder.getPlaylistsTitleText().setText(playlist.getName());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return playlistList != null ? playlistList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView playlistsCoverImage;
        private final TextView playlistsTitleText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistsCoverImage = itemView.findViewById(R.id.playlists_cover_image);
            playlistsTitleText = itemView.findViewById(R.id.playlists_title_text);
        }

        public ImageView getPlaylistsCoverImage() {
            return playlistsCoverImage;
        }

        public TextView getPlaylistsTitleText() {
            return playlistsTitleText;
        }
    }
}
