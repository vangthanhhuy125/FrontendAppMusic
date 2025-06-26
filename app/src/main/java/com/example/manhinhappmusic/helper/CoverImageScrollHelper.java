package com.example.manhinhappmusic.helper;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.widget.NestedScrollView;

import com.example.manhinhappmusic.view.ClearableEditText;

public class CoverImageScrollHelper {
    private final ImageView coverImage;
    private final NestedScrollView scrollView;

    private final float maxScrollDistance;
    private final float minScale;
    private final float translationFactor;

    public CoverImageScrollHelper(ImageView coverImage,
                                  NestedScrollView scrollView,
                                  float maxScrollDistance,
                                  float minScale,
                                  float translationFactor) {
        this.coverImage = coverImage;
        this.scrollView = scrollView;
        this.maxScrollDistance = maxScrollDistance;
        this.minScale = minScale;
        this.translationFactor = translationFactor;

        init();
    }

    private void init() {
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {

//                if(scrollY > 300)
//                {
                    float fraction = Math.min(scrollY / maxScrollDistance, 1f);

                    float scale = 1f - (1f - minScale) * fraction;

                    coverImage.setScaleX(scale);
                    coverImage.setScaleY(scale);

                    if(scale != minScale)
                    {
                        float translationY = scrollY * translationFactor;
                        coverImage.setTranslationY(translationY);
                    }
//                }




                Log.d("scroll", "" + scrollY);

            }
        });
    }
}
