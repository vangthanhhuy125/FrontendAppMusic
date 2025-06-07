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
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.R;

import java.util.List;

public class HomePlaylistAdapter extends RecyclerView.Adapter<HomePlaylistAdapter.ViewHolder> {

    private List<Playlist> playlistList;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;

    public HomePlaylistAdapter(List<Playlist> playlistList, OnItemClickListener onItemClickListener)
    {
        this.playlistList = playlistList;
        this.onItemClickListener = onItemClickListener;
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
                .load(playlist.getThumnailResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getPlaylistsCoverImage());
        holder.getPlaylistsTitleText().setText(playlist.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
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
