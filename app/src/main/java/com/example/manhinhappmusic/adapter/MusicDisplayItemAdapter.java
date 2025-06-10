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
import com.example.manhinhappmusic.model.MusicDisplayItem;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.fragment.MusicDisplayFragment;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.network.ApiService;

import java.util.List;

public class MusicDisplayItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ListItem> listItemList;
    private MusicDisplayItem.HomeDisplayType homeDisplayType;

    public  interface OnItemClickListener
    {
        void onItemClick(int position, ListItem item);
    }
    private OnItemClickListener onItemClickListener;
    public MusicDisplayItemAdapter(List<ListItem> listItemList, OnItemClickListener onItemClickListener, MusicDisplayItem.HomeDisplayType homeDisplayType)
    {
        this.listItemList = listItemList;
        this.onItemClickListener = onItemClickListener;
        this.homeDisplayType = homeDisplayType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(homeDisplayType == MusicDisplayItem.HomeDisplayType.MIX_PLAYLIST.MIX_PLAYLIST)
        {
            return new HomeMixPlaylistHolder(layoutInflater.inflate(R.layout.item_home_mix_playlist, parent,false));
        }
        else if(homeDisplayType == MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST)
        {
            return new HomeReleasePlaylistViewHolder(layoutInflater.inflate(R.layout.item_home_release_playlist, parent, false));
        }
        else if (homeDisplayType == MusicDisplayItem.HomeDisplayType.SONG)
        {
            return new HomeSongViewHolder(layoutInflater.inflate(R.layout.item_home_song, parent, false));
        }
        else
        {
            return  new HomeArtistHolder(layoutInflater.inflate(R.layout.item_home_artist, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HomeMixPlaylistHolder)
        {
            Playlist playlist = (Playlist) listItemList.get(position);
            HomeMixPlaylistHolder homeMixPlaylistHolder = (HomeMixPlaylistHolder) holder;
            Glide.with(holder.itemView.getContext())
                    .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(homeMixPlaylistHolder.getPlaylistCoverImage());
//            homeMixPlaylistHolder.getArtistNameText().setText(playlist.getArtistName() + "\n");
            homeMixPlaylistHolder.getArtistNameText().setText(playlist.getName() +"\n");

            homeMixPlaylistHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(homeMixPlaylistHolder.getAdapterPosition(), playlist);
                }
            });
        }
        else if(holder instanceof HomeSongViewHolder)
        {
            Song song = (Song) listItemList.get(position);
            HomeSongViewHolder homeSongViewHolder = (HomeSongViewHolder) holder;
            Glide.with(holder.itemView.getContext())
                    .load(ApiService.BASE_URL + song.getCoverImageUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(homeSongViewHolder.getSongCoverImage());
            homeSongViewHolder.getSongTitleText().setText(song.getTitle());
            homeSongViewHolder.getArtistNameText().setText(song.getArtistId());
            homeSongViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(homeSongViewHolder.getAdapterPosition(), song);
                }
            });
        }
        else if(holder instanceof HomeReleasePlaylistViewHolder) {
            Playlist playlist = (Playlist) listItemList.get(position);
            HomeReleasePlaylistViewHolder homeReleasePlaylistViewHolder = (HomeReleasePlaylistViewHolder) holder;
            Glide.with(holder.itemView.getContext())
                    .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(homeReleasePlaylistViewHolder.getPlaylistCoverImage());
            homeReleasePlaylistViewHolder.getPlaylistTitleText().setText(playlist.getName());
            homeReleasePlaylistViewHolder.getArtistNameText().setText(playlist.getArtistName());
            homeReleasePlaylistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(homeReleasePlaylistViewHolder.getAdapterPosition(), playlist);
                }
            });
        }
        else
        {
            User artist = (User) listItemList.get(position);
            HomeArtistHolder homeArtistHolder = (HomeArtistHolder) holder;
            Glide.with(holder.itemView.getContext())
                    .load(artist.getAvatarResID())
                    .circleCrop()
                    .into(homeArtistHolder.getArtistImage());
            homeArtistHolder.getArtistNameText().setText(artist.getFullName());
            homeArtistHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(homeArtistHolder.getAdapterPosition(), artist);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItemList.size();
    }

    public static class HomeSongViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView songCoverImage;
        private final TextView songTitleText;
        private final TextView artistNameText;
        public HomeSongViewHolder(@NonNull View itemView) {
            super(itemView);
            songCoverImage = itemView.findViewById(R.id.song_cover_image);
            songTitleText = itemView.findViewById(R.id.song_title_text);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
        }

        public ImageView getSongCoverImage() {
            return songCoverImage;
        }

        public TextView getSongTitleText() {
            return songTitleText;
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }
    }

    public static class HomeReleasePlaylistViewHolder extends  RecyclerView.ViewHolder {
        private final ImageView playlistCoverImage;
        private final TextView playlistTitleText;
        private final TextView artistNameText;
        public HomeReleasePlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistCoverImage = itemView.findViewById(R.id.playlist_cover_image);
            playlistTitleText = itemView.findViewById(R.id.playlist_title_text);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
        }

        public ImageView getPlaylistCoverImage() {
            return playlistCoverImage;
        }

        public TextView getPlaylistTitleText() {
            return playlistTitleText;
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }
    }

    public static  class HomeMixPlaylistHolder extends RecyclerView.ViewHolder {

        private final ImageView playlistCoverImage;
        private final TextView artistNameText;
        public HomeMixPlaylistHolder(@NonNull View itemView) {
            super(itemView);
            playlistCoverImage = itemView.findViewById(R.id.playlist_cover_image);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }

        public ImageView getPlaylistCoverImage() {
            return playlistCoverImage;
        }

    }

    public static class HomeArtistHolder extends RecyclerView.ViewHolder {
        private final ImageView artistImage;
        private final TextView artistNameText;
        public HomeArtistHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }

        public ImageView getArtistImage() {
            return artistImage;
        }
    }

    public List<ListItem> getListItemList() {
        return listItemList;
    }
}
