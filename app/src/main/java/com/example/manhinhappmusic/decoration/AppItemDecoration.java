package com.example.manhinhappmusic.decoration;

import android.content.res.Resources;
import android.util.TypedValue;

import androidx.recyclerview.widget.RecyclerView;

public abstract class AppItemDecoration extends RecyclerView.ItemDecoration {
    public static int convertDpToPx(int dp, Resources resources)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, resources.getDisplayMetrics());
    }
}
