package com.example.manhinhappmusic.ui.fragment.song;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Song;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import com.example.manhinhappmusic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NowPlayingSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowPlayingSongFragment extends BottomSheetDialogFragment {


    public NowPlayingSongFragment() {
        // Required empty public constructor
    }

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
    private ImageButton moreOptionButton;
    private SeekBar seekBar;
    private MaterialButton playButton;
    private Handler handler = new Handler();
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayerManager.getMediaPlayer() != null && mediaPlayerManager.getMediaPlayer().isPlaying()){
                seekBar.setProgress(mediaPlayerManager.getMediaPlayer().getCurrentPosition());
                handler.postDelayed(this, 200);
                currentPlayTimeTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getCurrentPosition()));
            }
        }
    };
    private MediaPlayerManager mediaPlayerManager;


    public static NowPlayingSongFragment newInstance(MediaPlayerManager mediaPlayerManager) {
        NowPlayingSongFragment fragment = new NowPlayingSongFragment();
        return fragment;
    }

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediaPlayerManager = MediaPlayerManager.getInstance(null);

        seekBar = view.findViewById(R.id.seekBar);
        songsCoverImage = view.findViewById(R.id.songs_cover_image);
        songsTitleTextView = view.findViewById(R.id.songs_name_text);
        songsArtistsNameTextView = view.findViewById(R.id.artist_name_text);
        playButton = view.findViewById(R.id.play_button);
        songDurationTextView = view.findViewById(R.id.duration_text);
        currentPlayTimeTextView = view.findViewById(R.id.current_time_text);
        shuffleButton = view.findViewById(R.id.shuffle_button);
        repeatButton = view.findViewById(R.id.repeat_button);
        skipNextButton = view.findViewById(R.id.skip_next_button);
        skipPreviousButton = view.findViewById(R.id.skip_previous_button);
        minimizeButton = view.findViewById(R.id.minimize_button);
        moreOptionButton = view.findViewById(R.id.more_options_button);

        mediaPlayerManager.addOnCompletionListener(new MediaPlayerManager.OnCompletionListener() {
            @Override
            public void onCompletion() {
                playButton.setIconResource(R.drawable.baseline_play_circle_24);
                handler.removeCallbacks(updateSeekBar);

                if(mediaPlayerManager.isPlayingNextSong())
                {
                    handler.post(updateSeekBar);
                    playButton.setIconResource(R.drawable.baseline_pause_circle_24);
                    setSongsInformation();
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
                setSongsInformation();
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
                    if(!mediaPlayerManager.getMediaPlayer().isPlaying()){

                        mediaPlayerManager.play();
                        handler.post(updateSeekBar);

                    }

                    else {
                        mediaPlayerManager.pause();
                        handler.removeCallbacks(updateSeekBar);

                    }
                }

            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayerManager.isShuffle)
                {
                    shuffleButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    mediaPlayerManager.shuffle();
                }
                else
                {
                    shuffleButton.setIconTint(ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
                }
                mediaPlayerManager.isShuffle = !mediaPlayerManager.isShuffle;
            }
        });

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
                dismiss();
            }
        });

        moreOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        handler.post(updateSeekBar);

    }

    private void setSongsInformation()
    {
        Glide.with(this.getContext())
                .load(mediaPlayerManager.getCurrentSong().getCoverImageUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(songsCoverImage);

        songsTitleTextView.setText(mediaPlayerManager.getCurrentSong().getTitle());
        songsArtistsNameTextView.setText(mediaPlayerManager.getCurrentSong().getArtistId());
        songDurationTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getDuration()));
        seekBar.setMax(mediaPlayerManager.getMediaPlayer().getDuration());

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