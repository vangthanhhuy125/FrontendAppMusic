package com.example.manhinhappmusic.adapter;

import static java.security.AccessController.getContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class HomePlaylistAdapter extends RecyclerView.Adapter<HomePlaylistAdapter.ViewHolder> {

    private List<Playlist> playlistList = new ArrayList<>();
    private String token;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener onItemClickListener;

    public HomePlaylistAdapter(List<Playlist> playlistList, OnItemClickListener onItemClickListener, String token) {
        if (playlistList != null)
            this.playlistList = playlistList;
        this.onItemClickListener = onItemClickListener;
        this.token = token;
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
    public void onBindViewHolder(HomePlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        String thumbnailUrl = playlist.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            String baseUrl = ApiClient.BASE_URL;
            if (baseUrl.endsWith("/") && thumbnailUrl.startsWith("/")) {
                thumbnailUrl = thumbnailUrl.substring(1); // bỏ dấu / đầu tiên
            }
            String fullUrl = baseUrl + thumbnailUrl;
            Log.d("HomePlaylistAdapter", "Full URL: " + fullUrl);

            Glide.with(holder.itemView.getContext())
                    .load(new GlideUrl(fullUrl, new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build()))
                    .apply(new RequestOptions().transform(
                            new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(holder.getPlaylistsCoverImage());
        }

        holder.getPlaylistsTitleText().setText(playlist.getName());
        holder.getPlaylistsSongCountText().setText(playlist.getSongs().size() + " bài hát");
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView playlistsCoverImage;
        private final TextView playlistsTitleText;
        private final TextView playlistsSongCountText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistsCoverImage = itemView.findViewById(R.id.playlists_cover_image);
            playlistsTitleText = itemView.findViewById(R.id.playlists_title_text);
            playlistsSongCountText = itemView.findViewById(R.id.playlists_count_text);
        }

        public ImageView getPlaylistsCoverImage() {
            return playlistsCoverImage;
        }

        public TextView getPlaylistsTitleText() {
            return playlistsTitleText;
        }

        public TextView getPlaylistsSongCountText() {
            return playlistsSongCountText;
        }
    }
}
