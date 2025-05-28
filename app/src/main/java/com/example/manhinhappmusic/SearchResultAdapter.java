package com.example.manhinhappmusic;

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

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> listItemList;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;
    public SearchResultAdapter(List<ListItem> listItemList, OnItemClickListener onItemClickListener)
    {
        this.listItemList = listItemList;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        return listItemList.get(position).getType().ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

       if(viewType == ListItemType.SONG.ordinal())
       {
           return new SongViewHolder(layoutInflater.inflate(R.layout.item_search_result_song, parent, false));
       }
       else if(viewType == ListItemType.ARTIST.ordinal())
       {
           return new ArtistViewHolder(layoutInflater.inflate(R.layout.item_search_result_artist, parent, false));

       }
       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SongViewHolder)
        {
            Song song = (Song)listItemList.get(position);
            SongViewHolder songViewHolder = (SongViewHolder) holder;
            Glide.with(holder.itemView.getContext())
                    .load(song.getCoverImageResID())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(songViewHolder.getSongCoverImage());
            songViewHolder.getSongTitleText().setText("Song • "+ song.getTitle());
            songViewHolder.getSongArtistText().setText(song.getArtistId());
            songViewHolder.getAddToPlaylistButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ((SongViewHolder) holder).getLinearContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        else if(holder instanceof ArtistViewHolder)
        {
            User artist = (User) listItemList.get(position);
            ArtistViewHolder artistViewHolder = (ArtistViewHolder)holder;
            Glide.with(holder.itemView.getContext())
                    .load(artist.getAvatarResID())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(80))))
                    .into(artistViewHolder.getArtistImage());
            artistViewHolder.getArtistNameText().setText(artist.getFullName());
            artistViewHolder.getLinearContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItemList.size();
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
