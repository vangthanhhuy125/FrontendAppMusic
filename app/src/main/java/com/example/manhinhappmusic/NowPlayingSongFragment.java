package com.example.manhinhappmusic;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NowPlayingSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowPlayingSongFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NowPlayingSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NowPlayingSongFragment.
     */
    // TODO: Rename and change types and number of parameters

    private TextView currentPlayTimeTextView;
    private TextView songDurationTextView;
    private ImageView songsCoverImage;
    private TextView songsTitleTextView;
    private TextView songsArtistsNameTextView;
    private MaterialButton shuffleButton;
    private MaterialButton repeatButton;
    private ImageButton skipPreviousButton;
    private ImageButton skipNextButton;
    private SeekBar seekBar;
    private MaterialButton playButton;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private MediaPlayerManager mediaPlayerManager;
    private Song song;
    private Playlist playlist;
    private int currentPosition;


    public NowPlayingSongFragment(Song song)
    {
        this.song = song;
        playlist = new Playlist("","", "",new ArrayList<>(Arrays.asList(song)),"", song.getCoverImageResID() );
    }

    public NowPlayingSongFragment(Playlist playlist, int currentPosition)
    {
        this.playlist = playlist;
        this.currentPosition = currentPosition;
        song = playlist.getSongsList().get(currentPosition);

    }

    public static NowPlayingSongFragment newInstance(String param1, String param2) {
        NowPlayingSongFragment fragment = new NowPlayingSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        mediaPlayerManager = new MediaPlayerManager(this.getContext(), playlist.getSongsList(), currentPosition, new MediaPlayerManager.OnCompletionListener() {
            @Override
            public void onCompletion() {
                song = mediaPlayerManager.getCurrentSong();
                setSongsInformation();
                updateSeekBar();
            }
        });

        setSongsInformation();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
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
                if(!mediaPlayerManager.getMediaPlayer().isPlaying()){

                        playButton.setIconResource(R.drawable.baseline_pause_circle_24);
                        mediaPlayerManager.play();
                        updateSeekBar();
                    }

                else {
                        playButton.setIconResource(R.drawable.baseline_play_circle_24);
                        mediaPlayerManager.pause();

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

    }

    private void updateSeekBar(){
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if(mediaPlayerManager.getMediaPlayer() != null && mediaPlayerManager.getMediaPlayer().isPlaying()){
                    seekBar.setProgress(mediaPlayerManager.getMediaPlayer().getCurrentPosition());
                    handler.postDelayed(this, 500);
                    currentPlayTimeTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getCurrentPosition()));
                }
            }
        };

        handler.post(updateSeekBar);
    }

    private void setSongsInformation()
    {
        songsCoverImage.setImageResource(song.getCoverImageResID());
        songsTitleTextView.setText(song.getTitle());
        songsArtistsNameTextView.setText(song.getArtistId());
        songDurationTextView.setText(formatTime(mediaPlayerManager.getMediaPlayer().getDuration()));
        seekBar.setMax(mediaPlayerManager.getMediaPlayer().getDuration());

    }

    private String formatTime(int milliseconds){
        int minutes = milliseconds / 1000 / 60;
        int seconds = milliseconds / 1000 % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerManager.clear();
    }
}