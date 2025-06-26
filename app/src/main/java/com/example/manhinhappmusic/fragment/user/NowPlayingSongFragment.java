package com.example.manhinhappmusic.fragment.user;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.manhinhappmusic.adapter.LyricAdapter;
import com.example.manhinhappmusic.databinding.FragmentNowPlayingSongBinding;
import com.example.manhinhappmusic.decoration.AppItemDecoration;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.model.LyricLine;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class NowPlayingSongFragment extends BottomSheetDialogFragment {


    public NowPlayingSongFragment() {
        // Required empty public constructor
    }

    private FragmentNowPlayingSongBinding binding;
    private TextView currentPlayTimeTextView;
    private TextView songDurationTextView;
    private ImageView songsCoverImage;
    private TextView songsTitleTextView;
    private TextView songsArtistsNameTextView;
    private MaterialButton shuffleButton;
    private MaterialButton repeatButton;
    private ImageButton skipPreviousButton;
    private ImageButton skipNextButton;
    private ImageButton minimizeButton;
//    private ImageButton moreOptionButton;
    private SeekBar seekBar;
    private MaterialButton playButton;
    private RecyclerView lyricsText;
    private LyricAdapter lyricAdapter;
    private List<LyricLine> lyricLines;
    private CardView currentLyricLayout;
    private TextView currentLyricText;
    private Handler handler = new Handler();
    private int currentLyricPosition;
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayerManager.isPrepared() && mediaPlayerManager.mediaPlayer.isPlaying()){
                int currentPosition = mediaPlayerManager.getMediaPlayer().getCurrentPosition();
                seekBar.setProgress(currentPosition);
                currentPlayTimeTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getCurrentPosition()));
                int newCurrentLyricPosition = getCurrentLyricPosition(currentPosition);
                lyricAdapter.setCurrentLyricPosition(newCurrentLyricPosition);
                LinearLayoutManager layoutManager = (LinearLayoutManager) lyricsText.getLayoutManager();
                if(currentLyricPosition != newCurrentLyricPosition)
                {
                    if(layoutManager.findFirstVisibleItemPosition() <= newCurrentLyricPosition && layoutManager.findLastVisibleItemPosition() >= newCurrentLyricPosition)
                    {
                        int recyclerViewHeight = lyricsText.getHeight();
                        View itemView = layoutManager.findViewByPosition(newCurrentLyricPosition);
                        if (itemView != null) {
                            int itemHeight = itemView.getHeight();
                            int offset = (recyclerViewHeight / 2) - (itemHeight / 2);
                            layoutManager.scrollToPositionWithOffset(newCurrentLyricPosition, offset);
                        }
                        currentLyricLayout.setVisibility(View.GONE);
                    }
                    else
                    {
                        currentLyricText.setText(lyricLines.get(newCurrentLyricPosition).getText());
                        currentLyricLayout.setVisibility(View.VISIBLE);
                    }


                }

                currentLyricPosition = newCurrentLyricPosition;
                handler.postDelayed(this, 200);
            }
        }
    };
    private MediaPlayerManager mediaPlayerManager;




    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);
            parent.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            parent.requestLayout();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNowPlayingSongBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediaPlayerManager = MediaPlayerManager.getInstance(null);

        seekBar = binding.seekBar;
        songsCoverImage = binding.songsCoverImage;
        songsTitleTextView = binding.songsNameText;
        songsArtistsNameTextView = binding.artistNameText;
        playButton = binding.playButton;
        songDurationTextView = binding.durationText;
        currentPlayTimeTextView = binding.currentTimeText;
        shuffleButton = binding.shuffleButton;
        repeatButton = binding.repeatButton;
        skipNextButton = binding.skipNextButton;
        skipPreviousButton = binding.skipPreviousButton;
        minimizeButton = binding.minimizeButton;
        lyricsText = binding.lyricsText;
        currentLyricLayout =  binding.currentLyricLayout;
        currentLyricText = binding.currentLyricText;

//        moreOptionButton = view.findViewById(R.id.more_options_button);

        lyricAdapter = new LyricAdapter(new ArrayList<>());
        lyricsText.setAdapter(lyricAdapter);
        lyricsText.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lyricsText.addItemDecoration(new VerticalLinearSpacingItemDecoration(AppItemDecoration.convertDpToPx(10, getResources())));


        mediaPlayerManager.addOnCompletionListener(new MediaPlayerManager.OnCompletionListener() {
            @Override
            public void onCompletion() {
                playButton.setIconResource(R.drawable.baseline_play_circle_24);

                if(mediaPlayerManager.isPlayingNextSong())
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mediaPlayerManager.isPrepared()){
                                handler.removeCallbacks(this);
                                handler.post(updateSeekBar);
                                setSongsInformation();

                            }
                            else
                                handler.postDelayed(this,200);
                        }
                    });
                    playButton.setIconResource(R.drawable.baseline_pause_circle_24);
                }
            }
        }, NowPlayingSongFragment.class.getName());
        mediaPlayerManager.addOnPlayingStateChangeListener(new MediaPlayerManager.OnPlayingStateChangeListener() {
            @Override
            public void onPlayingStateChange(boolean isPlaying) {
                if(isPlaying)
                {
                    playButton.setIconResource(R.drawable.baseline_pause_circle_24);
                }
                else {
                    playButton.setIconResource(R.drawable.baseline_play_circle_24);
                }
            }
        }, NowPlayingSongFragment.class.getName());

        mediaPlayerManager.addOnPlayingSongChangeListener(new MediaPlayerManager.OnPlayingSongChangeListener() {
            @Override
            public void onPlayingSongChange(Song song) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mediaPlayerManager.isPrepared()){
                            setSongsInformation();
                            handler.removeCallbacks(this);
                            handler.post(updateSeekBar);
                        }
                        else
                            handler.postDelayed(this,200);
                    }
                });
            }
        }, NowPlayingSongFragment.class.getName());

        setSongsInformation();

        if(mediaPlayerManager.getMediaPlayer() != null && !mediaPlayerManager.getMediaPlayer().isPlaying())
        {
            playButton.setIconResource(R.drawable.baseline_play_circle_24);
            seekBar.setProgress(mediaPlayerManager.getMediaPlayer().getCurrentPosition());
            currentPlayTimeTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getCurrentPosition()));
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser && mediaPlayerManager.getMediaPlayer() != null){
                    mediaPlayerManager.getMediaPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayerManager != null && mediaPlayerManager.getMediaPlayer() != null)
                {
                    if(mediaPlayerManager.isPrepared() && !mediaPlayerManager.getMediaPlayer().isPlaying()){

                        mediaPlayerManager.play();
                        handler.post(updateSeekBar);

                    }

                    else if(mediaPlayerManager.isPrepared()){
                        mediaPlayerManager.pause();

                    }
                }

            }
        });

        if(mediaPlayerManager.isShuffle())
        {
            shuffleButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

        }
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isShuffle = mediaPlayerManager.isShuffle();
                if(!isShuffle)
                {
                    shuffleButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                }
                else
                {
                    shuffleButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
                }
                mediaPlayerManager.setShuffle(!isShuffle);
            }
        });

        switch (mediaPlayerManager.getRepeatMode())
        {
            case REPEAT_ALL:
                repeatButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                break;
            case REPEAT_ONE:
                repeatButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                repeatButton.setIconResource(R.drawable.baseline_repeat_one_24);
                break;
        }
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mediaPlayerManager.getRepeatMode()){
                    case NONE:
                        repeatButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        mediaPlayerManager.setRepeatMode(MediaPlayerManager.RepeatMode.REPEAT_ALL);
                        break;
                    case REPEAT_ALL:
                        repeatButton.setIconResource(R.drawable.baseline_repeat_one_24);
                        mediaPlayerManager.setRepeatMode(MediaPlayerManager.RepeatMode.REPEAT_ONE);
                        break;
                    case REPEAT_ONE:
                        repeatButton.setIconResource(R.drawable.baseline_repeat_24);
                        repeatButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
                        mediaPlayerManager.setRepeatMode(MediaPlayerManager.RepeatMode.NONE);
                        break;
                }

            }
        });

        skipNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerManager.skipNext();
            }
        });

        skipPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerManager.skipPrevious();
            }
        });

        minimizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerManager.removeOnCompletionListener(NowPlayingSongFragment.class.getName());
                mediaPlayerManager.removeOnPlayingStateChangeListener(NowPlayingSongFragment.class.getName());
                mediaPlayerManager.removeOnPlayingSongChangeListener(NowPlayingSongFragment.class.getName());
                handler.removeCallbacks(updateSeekBar);
                dismiss();
            }
        });

//        moreOptionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        handler.post(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayerManager.isPrepared()){
                    setSongsInformation();
                    handler.removeCallbacks(this);
                    handler.post(updateSeekBar);
                }
                else
                    handler.postDelayed(this,200);
            }
        });

    }

    private void setSongsInformation()
    {
        try {
            Song song = mediaPlayerManager.getCurrentSong();
            if(song.getCoverImageUrl() != null && !song.getCoverImageUrl().isEmpty())
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
                                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                            new int[]{vibrant,
                                                    Color.parseColor("#121212"),
                                                    Color.parseColor("#121212"),
                                                    Color.parseColor("#121212"),
                                            });
                                    binding.mainLayout.setBackground(gradientDrawable);
                                    binding.lyricsLayout.setBackgroundTintList(ColorStateList.valueOf(vibrant));

                                });


                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            else
                Glide.with(this.getContext())
                        .load(R.drawable.music_default_cover)
                        .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                        .into(songsCoverImage);

            songsTitleTextView.setText(song.getTitle());
            //songsArtistsNameTextView.setText(mediaPlayerManager.getCurrentSong().getArtistId());
            songDurationTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getDuration()));
            seekBar.setMax(mediaPlayerManager.getMediaPlayer().getDuration());
            lyricLines = LyricLine.parseLrc(getResources().openRawResource(R.raw.ocean_view_lyrics));
            lyricAdapter.clearSungLyric();
            if(mediaPlayerManager.isPrepared())
            {
                lyricAdapter.setLyrics(LyricLine.parseToStrings(lyricLines));

            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {

        }


    }

    private int getCurrentLyricPosition(int position)
    {
        for (int i = 0; i < lyricLines.size(); i++) {
            LyricLine line = lyricLines.get(i);
            long nextTime = (i + 1 < lyricLines.size()) ? lyricLines.get(i + 1).getTime() : Long.MAX_VALUE;

            if (position >= line.getTime() && position < nextTime) {
                return  i;
            }
        }
        return 0;

    }

    private List<Integer> getAllSungLyricPosition(int position)
    {
        List<Integer> allPosition = new ArrayList<>();
        for (int i = 0; i < lyricLines.size(); i++) {
            LyricLine line = lyricLines.get(i);
            long nextTime = (i + 1 < lyricLines.size()) ? lyricLines.get(i + 1).getTime() : Long.MAX_VALUE;

            if (position >= line.getTime()) {
                allPosition.add(Integer.valueOf(i));
            }
        }
        return allPosition;
    }

    private String formatTime(int milliseconds){
        int minutes = milliseconds / 1000 / 60;
        int seconds = milliseconds / 1000 % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        mediaPlayerManager.removeOnCompletionListener(NowPlayingSongFragment.class.getName());
        mediaPlayerManager.removeOnPlayingStateChangeListener(NowPlayingSongFragment.class.getName());
        mediaPlayerManager.removeOnPlayingSongChangeListener(NowPlayingSongFragment.class.getName());
        handler.removeCallbacks(updateSeekBar);
    }

    public void setMediaPlayerManager(MediaPlayerManager mediaPlayerManager) {
        if(this.mediaPlayerManager != null)
            this.mediaPlayerManager.getMediaPlayer().release();
        this.mediaPlayerManager = mediaPlayerManager;
    }


}