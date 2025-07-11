package com.example.manhinhappmusic.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalLinearSpacingItemDecoration extends AppItemDecoration{
    private final int linearSpacing;

    public VerticalLinearSpacingItemDecoration(int linearSpacing)
    {
        this.linearSpacing = linearSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = linearSpacing;
    }
}
