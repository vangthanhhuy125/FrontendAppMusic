package com.example.manhinhappmusic.fragment;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MiniPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiniPlayerFragment extends BaseFragment {


    private ImageView songsCoverImage;
    private TextView songsNameText;
    private TextView artistsNameText;
    private SeekBar seekBar;
    private ImageButton skipPreviousButton;
    private ImageButton playButton;
    private ImageButton skipNextButton;

    private CardView miniPlayerBackground;


    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayerManager.isPrepared() && mediaPlayerManager.mediaPlayer.isPlaying()){
                seekBar.setProgress(mediaPlayerManager.getMediaPlayer().getCurrentPosition());
                handler.postDelayed(this, 200);
            }
        }
    };
    private Handler handler = new Handler();

    private MediaPlayerManager mediaPlayerManager;


    public MiniPlayerFragment() {
        // Required empty public constructor
    }


    public static MiniPlayerFragment newInstance(Playlist playlist, int currentPosition) {
        MiniPlayerFragment fragment = new MiniPlayerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mini_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songsCoverImage = view.findViewById(R.id.songs_cover_image);
        songsNameText = view.findViewById(R.id.songs_name_text);
        artistsNameText = view.findViewById(R.id.artist_name_text);
        miniPlayerBackground = view.findViewById(R.id.mini_player_background);
        seekBar = view.findViewById(R.id.seekBar);
        skipPreviousButton = view.findViewById(R.id.skip_previous_button);
        playButton = view.findViewById(R.id.play_button);
        skipNextButton = view.findViewById(R.id.skip_next_button);

        mediaPlayerManager = MediaPlayerManager.getInstance(null);

        mediaPlayerManager.addOnCompletionListener(new MediaPlayerManager.OnCompletionListener() {
            @Override
            public void onCompletion() {
                playButton.setImageResource(R.drawable.baseline_play_circle_24);
                handler.removeCallbacks(updateSeekBar);

                if(mediaPlayerManager.isPlayingNextSong())
                {
                    handler.post(updateSeekBar);
                    playButton.setImageResource(R.drawable.baseline_pause_circle_24);
                    setSongsInformation();
                }
            }
        }, MiniPlayerFragment.class.getName());

        mediaPlayerManager.addOnPlayingStateChangeListener(new MediaPlayerManager.OnPlayingStateChangeListener() {
            @Override
            public void onPlayingStateChange(boolean isPlaying) {
                if(isPlaying)
                {
                    playButton.setImageResource(R.drawable.baseline_pause_circle_24);
                }
                else {
                    playButton.setImageResource(R.drawable.baseline_play_circle_24);
                }
            }
        }, MiniPlayerFragment.class.getName());

        mediaPlayerManager.addOnPlayingSongChangeListener(new MediaPlayerManager.OnPlayingSongChangeListener() {
            @Override
            public void onPlayingSongChange(Song song) {
                setSongsInformation();
            }
        }, MiniPlayerFragment.class.getName());


        skipPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerManager.skipPrevious();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayerManager.getMediaPlayer().isPlaying())
                {
                    if(mediaPlayerManager.isPrepared())
                    {
                        handler.post(updateSeekBar);
                        mediaPlayerManager.play();
                    }

                }
                else
                {
                    if(mediaPlayerManager.isPrepared())
                    {
                        handler.removeCallbacks(updateSeekBar);
                        mediaPlayerManager.pause();
                    }

                }

            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerManager.skipNext();
            }
        });



        miniPlayerBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestOpenBottomSheetFragment(FragmentTag.NOW_PLAYING_SONG);
            }
        });


        handler.post(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayerManager.isPrepared()){
                    setSongsInformation();
                    handler.removeCallbacks(this);
                    handler.post(updateSeekBar);
                }
                handler.postDelayed(this,200);
            }
        });


    }

    private void setSongsInformation()
    {
        Glide.with(this.getContext())
                .asBitmap()
                .load(ApiService.BASE_URL + mediaPlayerManager.getCurrentSong().getCoverImageUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        songsCoverImage.setImageBitmap(resource);
                        Palette.from(resource).generate(palette -> {
                            int vibrant = palette.getVibrantColor(Color.GRAY);
                            miniPlayerBackground.setBackgroundTintList(ColorStateList.valueOf(vibrant));
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        songsNameText.setText(mediaPlayerManager.getCurrentSong().getTitle());
        artistsNameText.setText(mediaPlayerManager.getCurrentSong().getArtistId());
        seekBar.setMax(mediaPlayerManager.getMediaPlayer().getDuration());
//        Palette.from(BitmapFactory.decodeStream(this.getContext().getResources().openRawResource(mediaPlayerManager.getCurrentSong().getCoverImageResID()))).generate(palette -> {
//            int vibrant = palette.getVibrantColor(Color.GRAY);
//            miniPlayerBackground.setBackgroundTintList(ColorStateList.valueOf(vibrant));
//        });
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerManager.removeOnCompletionListener(MiniPlayerFragment.class.getName());
        mediaPlayerManager.removeOnPlayingStateChangeListener(MiniPlayerFragment.class.getName());
        mediaPlayerManager.removeOnPlayingSongChangeListener(MiniPlayerFragment.class.getName());
    }
}