package com.example.manhinhappmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.manhinhappmusic.model.User;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SongViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView songCoverImage;
        private final TextView songTitleText;
        private final TextView songArtistText;
        private final ImageButton addToPlaylistButton;

        private final LinearLayout linearContainer;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songCoverImage = itemView.findViewById(R.id.song_cover_image);
            songTitleText = itemView.findViewById(R.id.song_title_text);
            songArtistText = itemView.findViewById(R.id.artist_name_text);
            addToPlaylistButton = itemView.findViewById(R.id.add_to_playlist_button);
            linearContainer = itemView.findViewById(R.id.linear_container);
        }

        public ImageView getSongCoverImage() {
            return songCoverImage;
        }
        public TextView getSongTitleText() {
            return songTitleText;
        }
        public TextView getSongArtistText() {
            return songArtistText;
        }
        public ImageButton getAddToPlaylistButton() {
            return addToPlaylistButton;
        }
        public LinearLayout getLinearContainer() {
            return linearContainer;
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{

        private final ImageView artistImage;
        private final TextView artistNameText;
        private final LinearLayout linearContainer;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
            linearContainer = itemView.findViewById(R.id.linear_container);
        }

        public ImageView getArtistImage() {
            return artistImage;
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }

        public LinearLayout getLinearContainer() {
            return linearContainer;
        }
    }
}
