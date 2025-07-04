package com.example.manhinhappmusic.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.R;

import java.lang.reflect.Array;
import java.util.List;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.ViewHolder> {

    private List<Genre> genreList;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public OnItemClickListener onItemClickListener;

    private static int[] colors = new int[]{
            R.color.red,
            R.color.amber,
            R.color.pigment_green,
            R.color.process_cyan,
            R.color.magenta_dye
    };

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView browseImage;
        private final TextView browseText;
        private final CardView browseHolderCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            browseImage = itemView.findViewById(R.id.browse_img);
            browseText = itemView.findViewById(R.id.browse_txt);
            browseHolderCardView = itemView.findViewById(R.id.browse_holder_cardview);
        }

        public ImageView getBrowseImage() {
            return browseImage;
        }

        public TextView getBrowseText() {
            return browseText;
        }

        public CardView getBrowseHolderCardView() {
            return browseHolderCardView;
        }
    }

    public BrowseAdapter(List<Genre> genreList, OnItemClickListener onItemClickListener){
        this.genreList = genreList;
        this.onItemClickListener = onItemClickListener;
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
        Genre genre = genreList.get(position);
        holder.getBrowseText().setText(genre.getName());
        Glide.with(holder.itemView.getContext())
                .load(genre.getThumbnailUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getBrowseImage());
        holder.getBrowseHolderCardView().setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.getContext(), colors[position % Array.getLength(colors)])));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }


}
