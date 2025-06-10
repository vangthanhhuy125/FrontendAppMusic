package com.example.manhinhappmusic.adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiService;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ListItem> listItemList;
    private SparseBooleanArray checkStates;
    public interface OnItemClickListener{
        void onItemClick(int position, ListItem item);
    }
    public interface OnItemCheckBoxClickListener{
        void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox);
    }
    private OnItemClickListener onItemClickListener;
    private OnItemCheckBoxClickListener onItemCheckedCheckBoxClickListener;
    private OnItemCheckBoxClickListener onItemUncheckedCheckBoxClickListener;
    public SearchResultAdapter(List<ListItem> listItemList, SparseBooleanArray checkStates,OnItemClickListener onItemClickListener)
    {
        this.listItemList = listItemList;
        this.checkStates = checkStates;
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
       else if(viewType == ListItemType.PLAYLIST.ordinal())
       {
           return new PlaylistViewHolder(layoutInflater.inflate(R.layout.item_search_result_playlist, parent, false));

       }
       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SongViewHolder)
        {
            Song song = (Song)listItemList.get(position);
            SongViewHolder songViewHolder = (SongViewHolder) holder;
            if(song.getCoverImageUrl() != null && !song.getCoverImageUrl().isEmpty())
                Glide.with(holder.itemView.getContext())
                    .load(ApiService.BASE_URL + song.getCoverImageUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(songViewHolder.getSongCoverImage());
            else
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.music_default_cover)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                        .into(songViewHolder.getSongCoverImage());
            songViewHolder.getSongTitleText().setText("Song • "+ song.getTitle());
            songViewHolder.getSongArtistText().setText(song.getArtistId());
            CheckBox checkBox = songViewHolder.getCheckBox();
            checkBox.setChecked(checkStates.get(position));
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkStates.put(songViewHolder.getAdapterPosition(),checkBox.isChecked() );
                    if(checkBox.isChecked())
                    {
                        if(onItemCheckedCheckBoxClickListener != null)
                        {
                            onItemCheckedCheckBoxClickListener.onItemCheckBoxClick(songViewHolder.getAdapterPosition(), song, checkBox);
                        }
                    }
                    else
                    {

                        if(onItemUncheckedCheckBoxClickListener != null)
                        {
                            onItemUncheckedCheckBoxClickListener.onItemCheckBoxClick(songViewHolder.getAdapterPosition(), song, checkBox);
                        }
                    }

                }
            });
            songViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), song);
                }
            });
        }
        else if(holder instanceof ArtistViewHolder)
        {
            User artist = (User) listItemList.get(position);
            ArtistViewHolder artistViewHolder = (ArtistViewHolder)holder;
//            Glide.with(holder.itemView.getContext())
//                    .load(ApiService.BASE_URL + artistViewHolder.ge())
//                    .circleCrop()
//                    .into(artistViewHolder.getArtistImage());
            artistViewHolder.getArtistNameText().setText(artist.getFullName());
            artistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), artist);
                }
            });
        } else if (holder instanceof PlaylistViewHolder) {
            Playlist playlist = (Playlist) listItemList.get(position);
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;
            if(playlist.getThumbnailUrl() != null && !playlist.getThumbnailUrl().isEmpty())
                Glide.with(holder.itemView.getContext())
                    .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(playlistViewHolder.getPlaylistCoverImage());
            else
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.music_default_cover)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                        .into(playlistViewHolder.getPlaylistCoverImage());
            playlistViewHolder.getPlaylistTitleText().setText(playlist.getName());
            CheckBox checkBox = playlistViewHolder.getCheckBox();
            checkBox.setChecked(checkStates.get(position));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkStates.put(playlistViewHolder.getAdapterPosition(),checkBox.isChecked() );

                    if(checkBox.isChecked())
                    {
                        if(onItemCheckedCheckBoxClickListener != null)
                        {
                            onItemCheckedCheckBoxClickListener.onItemCheckBoxClick(playlistViewHolder.getAdapterPosition(), playlist, checkBox);
                        }
                    }
                    else
                    {
//                        checkBox.setChecked(true);
                        if(onItemUncheckedCheckBoxClickListener != null)
                        {
                            onItemUncheckedCheckBoxClickListener.onItemCheckBoxClick(playlistViewHolder.getAdapterPosition(), playlist, checkBox);
                        }
                    }
                }
            });

            playlistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), playlist);
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
        private final CheckBox checkBox;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songCoverImage = itemView.findViewById(R.id.song_cover_image);
            songTitleText = itemView.findViewById(R.id.song_title_text);
            songArtistText = itemView.findViewById(R.id.artist_name_text);
            checkBox = itemView.findViewById(R.id.checkbox);
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

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{

        private final ImageView artistImage;
        private final TextView artistNameText;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
        }

        public ImageView getArtistImage() {
            return artistImage;
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }


    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private final ImageView playlistCoverImage;
        private final TextView playlistTitleText;
        private final CheckBox checkBox;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistCoverImage = itemView.findViewById(R.id.playlist_cover_image);
            playlistTitleText = itemView.findViewById(R.id.playlist_title_text);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public ImageView getPlaylistCoverImage() {
            return playlistCoverImage;
        }

        public TextView getPlaylistTitleText() {
            return playlistTitleText;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    public List<ListItem> getListItemList() {
        return listItemList;
    }

    public void setOnItemCheckBoxClickListener(OnItemCheckBoxClickListener onItemCheckBoxClickListener, boolean checkedState) {
        if(checkedState)
        {
            onItemCheckedCheckBoxClickListener = onItemCheckBoxClickListener;
        }
        else
        {
            onItemUncheckedCheckBoxClickListener = onItemCheckBoxClickListener;
        }
    }

    public void setNewData(List<ListItem> listItemList, SparseBooleanArray checkStates) {
        this.listItemList = listItemList;
        this.checkStates = checkStates;
    }

    public SparseBooleanArray getCheckStates() {
        return checkStates;
    }
}
