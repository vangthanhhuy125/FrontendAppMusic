package com.example.manhinhappmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.manhinhappmusic.api.common.CommonSongApi;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private final List<String> songIds;
    private static OnItemClickListener itemClickListener;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public SongAdapter(Context context, List<String> songIds, OnItemClickListener itemClickListener) {
        this.context = context;
        this.songIds = songIds;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String songId = songIds.get(position);

        // Gọi API để lấy thông tin bài hát từ songId
        CommonSongApi apiService = ApiClient.getRetrofitWithAuth(context).create(CommonSongApi.class);
        apiService.getSong(songId).enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Song song = response.body();
                    holder.getTextViewArtist().setText(song.getArtistId());
                    holder.getTextViewSongTitle().setText(song.getTitle());
                    Glide.with(holder.itemView.getContext())
                            .load(song.getCoverImageUrl())
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                            .into(holder.getImageViewThumbnail());
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                // Xử lý lỗi nếu gọi API thất bại
            }
        });
    }

    @Override
    public int getItemCount() {
        return (songIds != null) ? songIds.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewArtist;
        private final TextView textViewSongTitle;
        private final ImageButton moreOptionsButton;
        private final ImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewArtist = itemView.findViewById(R.id.txtArtist);
            textViewSongTitle = itemView.findViewById(R.id.txtSongTitle);
            moreOptionsButton = itemView.findViewById(R.id.more_options_button);
            imageViewThumbnail = itemView.findViewById(R.id.imgThumbnail);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            });
            moreOptionsButton.setOnClickListener(v -> {
                // Thực hiện các thao tác khác nếu cần
            });
        }

        public TextView getTextViewArtist() {
            return textViewArtist;
        }

        public TextView getTextViewSongTitle() {
            return textViewSongTitle;
        }

        public ImageButton getMoreOptionsButton() {
            return moreOptionsButton;
        }

        public ImageView getImageViewThumbnail() {
            return imageViewThumbnail;
        }
    }

    public List<String> getSongList() {
        return songIds;
    }
}
