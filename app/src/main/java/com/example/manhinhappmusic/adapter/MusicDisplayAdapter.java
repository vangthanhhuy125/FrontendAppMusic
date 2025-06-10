package com.example.manhinhappmusic.adapter;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.decoration.HorizontalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.MusicDisplayFragment;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.MusicDisplayItem;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicDisplayAdapter extends RecyclerView.Adapter<MusicDisplayAdapter.ViewHolder> {

    private List<MusicDisplayItem> items;
    private OnDisplayItemCLickListener onDisplayItemCLickListener;
    public interface OnDisplayItemCLickListener{
        void onDisplayItemClick(int position, ListItem item);
    }
    public MusicDisplayAdapter(List<MusicDisplayItem> items, OnDisplayItemCLickListener onDisplayItemCLickListener)
    {
        this.items = items;
        this.onDisplayItemCLickListener = onDisplayItemCLickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()
                ).inflate(R.layout.item_music_display, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicDisplayItem item = items.get(position);
        holder.getTitleText().setText(item.getTitle());
        RecyclerView itemDisplayView = holder.getItemDisplayView();
        MusicDisplayItemAdapter adapter = new MusicDisplayItemAdapter(item.getItems(), new MusicDisplayItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ListItem item) {
                onDisplayItemCLickListener.onDisplayItemClick(position, item);
            }
        }, item.getHomeDisplayType());
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemDisplayView.setAdapter(adapter);
        itemDisplayView.setLayoutManager(layoutManager);
        itemDisplayView.addItemDecoration(new HorizontalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, holder.itemView.getResources().getDisplayMetrics())));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final RecyclerView itemDisplayView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            itemDisplayView = itemView.findViewById(R.id.item_display_view);
        }

        public RecyclerView getItemDisplayView() {
            return itemDisplayView;
        }

        public TextView getTitleText() {
            return titleText;
        }
    }

    public List<MusicDisplayItem> getItems() {
        return items;
    }

    public void setItems(List<MusicDisplayItem> items) {
        this.items = items;
    }
}
