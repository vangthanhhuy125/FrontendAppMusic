package com.example.manhinhappmusic.adapter;

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
import com.example.manhinhappmusic.model.Playlist;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPlaylistAddSongAdapter extends RecyclerView.Adapter<SearchPlaylistAddSongAdapter.PlaylistViewHolder> {

    private List<Playlist> playlistList;
    private Map<String, Boolean> checkStates = new HashMap<>();
    private boolean isAllCheck;
    public interface OnItemClickListener
    {
        void onItemClick(int position, Playlist item);
    }
    private OnItemClickListener onItemClickListener;
    public SearchPlaylistAddSongAdapter(List<Playlist> playlistList, OnItemClickListener onItemClickListener, boolean isALLChecked)
    {
        this.playlistList = playlistList;
        this.isAllCheck = isALLChecked;
        for(Playlist playlist: playlistList)
        {
            if(isALLChecked)
            {
                checkStates.put(playlist.getId(), true);
            }
            else
            {
                checkStates.put(playlist.getId(), false);
            }
        }
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_playlist_add_song, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        holder.getPlaylistCoverImage().setImageResource(playlist.getThumnailResID());
        if(playlist.getThumnailResID() != 0)
            Glide.with(holder.itemView.getContext())
                .load(playlist.getThumnailResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getPlaylistCoverImage());
        holder.getPlaylistTitleText().setText(playlist.getName());
        CheckBox checkBox = holder.getCheckBox();
        checkBox.setChecked(checkStates.get(playlist.getId()));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStates.put(playlist.getId(), checkBox.isChecked());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                checkBox.setChecked(!checkBox.isChecked());
                onItemClickListener.onItemClick(holder.getAdapterPosition(), playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
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

    public List<Playlist> getPlaylistList() {
        return Collections.unmodifiableList(playlistList);
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
        for(Playlist playlist: playlistList)
        {
            if(!checkStates.containsKey(playlist.getId()))
            {
                checkStates.put(playlist.getId(), false);
            }
        }
    }
    public void addPlaylist(Playlist playlist)
    {
        playlistList.add(playlist);
        checkStates.put(playlist.getId(), false);
    }

    public Map<String, Boolean> getCheckStates() {
        return checkStates;
    }
}
