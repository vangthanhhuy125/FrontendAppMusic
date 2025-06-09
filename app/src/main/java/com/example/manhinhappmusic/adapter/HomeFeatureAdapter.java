package com.example.manhinhappmusic.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFeatureAdapter extends RecyclerView.Adapter<HomeFeatureAdapter.ViewHolder>{

    private List<Playlist> playlistList;
    private final String token;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;

    public HomeFeatureAdapter(String token, List<Playlist> playlistList, OnItemClickListener onItemClickListener)
    {
        this.token = token;
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
                .inflate(R.layout.item_featuring, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        String thumbnailUrl = playlist.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            String baseUrl = ApiClient.BASE_URL;
            if (baseUrl.endsWith("/") && thumbnailUrl.startsWith("/")) {
                thumbnailUrl = thumbnailUrl.substring(1); // bỏ dấu / đầu tiên
            }
            String fullUrl = baseUrl + thumbnailUrl;
            Log.d("HomePlaylistAdapter", "Loading image from URL: " + fullUrl);

            Glide.with(holder.itemView.getContext())
                    .load(new GlideUrl(fullUrl, new LazyHeaders.Builder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build()))
                    .apply(new RequestOptions().transform(
                            new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(holder.getFeaturesCoverImage());
        }
        holder.getFeaturesTitleText().setText(playlist.getName());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        if (playlistList == null) return 0;
        return playlistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView featuresCoverImage;
        private final TextView featuresTitleText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featuresCoverImage = itemView.findViewById(R.id.features_cover_image);
            featuresTitleText = itemView.findViewById(R.id.features_title_text);
        }

        public ImageView getFeaturesCoverImage() {
            return featuresCoverImage;
        }

        public TextView getFeaturesTitleText() {
            return featuresTitleText;
        }
    }
}
