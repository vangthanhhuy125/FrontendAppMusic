package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private List<Genre> borwseItems;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView browseImage;
        private final TextView browseText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            browseImage = itemView.findViewById(R.id.browse_img);
            browseText = itemView.findViewById(R.id.browse_txt);
        }

        public ImageView getBrowseImage() {
            return browseImage;
        }

        public TextView getBrowseText() {
            return browseText;
        }
    }

    public BrowseAdapter(List<Genre> browseItems){
        this.borwseItems = browseItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_browse, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = borwseItems.get(position);
        holder.getBrowseText().setText(genre.getName());
        holder.getBrowseImage().setImageResource(R.drawable.avatar_app_music);
//        ViewGroup.LayoutParams lp = holder.getBrowseImage().getLayoutParams();
//        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
//            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
//            flexboxLp.setFlexGrow(1.0f);
//            flexboxLp.setAlignSelf(AlignSelf.AUTO);
//        }
    }

    @Override
    public int getItemCount() {
        return borwseItems.size();
    }


}
