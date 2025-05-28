package com.example.manhinhappmusic;

import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MiniPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiniPlayerFragment extends BaseFragment {


    private ShapeableImageView songsCoverImage;
    private TextView songsNameText;
    private TextView artistsNameText;
    private SeekBar seekBar;
    private ImageButton skipPreviousButton;
    private ImageButton playButton;
    private ImageButton skipNextButton;

    private CardView miniPlayerBackground;

    private Playlist playlist;
    private int currentPosition;
    private  Song currentSong;

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayerManager.getMediaPlayer() != null && mediaPlayerManager.getMediaPlayer().isPlaying()){
                seekBar.setProgress(mediaPlayerManager.getMediaPlayer().getCurrentPosition());
                handler.postDelayed(this, 200);
            }
        }
    };
    private Handler handler = new Handler();

    private  MediaPlayerManager mediaPlayerManager;

    private MiniPlayerViewModel viewModel;

    public MiniPlayerFragment() {
        // Required empty public constructor
    }

    public MiniPlayerFragment(MediaPlayerManager mediaPlayerManager)
    {
        this.mediaPlayerManager = mediaPlayerManager;
    }

    public static MiniPlayerFragment newInstance(Playlist playlist, int currentPosition) {
        MiniPlayerFragment fragment = new MiniPlayerFragment();
        fragment.setPlaylist(playlist);
        fragment.setCurrentPosition(currentPosition);
        fragment.setCurrentSong(playlist.getSongsList().get(currentPosition));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MiniPlayerViewModel.class);
        if(viewModel.getMediaPlayerManager() == null && playlist != null && currentSong != null)
        {
            viewModel.setPlaylist(playlist);
            viewModel.setCurrentPosition(currentPosition);
            viewModel.setCurrentSong(currentSong);
            mediaPlayerManager = new MediaPlayerManager(this.getContext(), playlist.getSongsList(), currentPosition);
            viewModel.setMediaPlayerManager(mediaPlayerManager);
        }
        else {
            playlist = viewModel.getPlaylist();
            currentPosition = viewModel.getCurrentPosition();
            currentSong = viewModel.getCurrentSong();
            mediaPlayerManager = viewModel.getMediaPlayerManager();
        }
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
        songsCoverImage.setImageResource(currentSong.getAudioResID());
        songsNameText = view.findViewById(R.id.songs_name_text);
        songsNameText.setText(currentSong.getTitle());
        artistsNameText = view.findViewById(R.id.artist_name_text);
        artistsNameText.setText(currentSong.getArtistId());
        miniPlayerBackground = view.findViewById(R.id.mini_player_background);
        seekBar = view.findViewById(R.id.seekBar);
        skipPreviousButton = view.findViewById(R.id.skip_previous_button);
        playButton = view.findViewById(R.id.play_button);
        skipNextButton = view.findViewById(R.id.skip_next_button);

        mediaPlayerManager.addOnCompletionListeners(new MediaPlayerManager.OnCompletionListener() {
            @Override
            public void onCompletion() {
                playButton.setImageResource(R.drawable.baseline_play_circle_24);
                handler.removeCallbacks(updateSeekBar);

                if(mediaPlayerManager.isPlayingNextSong())
                {
                    currentSong = mediaPlayerManager.getCurrentSong();
                    handler.post(updateSeekBar);
                    playButton.setImageResource(R.drawable.baseline_pause_circle_24);
                    setSongsInformation();
                }
            }
        });

        setSongsInformation();

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
                    playButton.setImageResource(R.drawable.baseline_pause_circle_24);
                    handler.post(updateSeekBar);
                    mediaPlayerManager.play();
                }
                else
                {
                    playButton.setImageResource(R.drawable.baseline_play_circle_24);
                    handler.removeCallbacks(updateSeekBar);
                    mediaPlayerManager.pause();
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
                callback.onRequestOpenBottomSheetFragment(FragmentTag.NOW_PLAYING_SONG, mediaPlayerManager);
            }
        });


        handler.post(updateSeekBar);
        mediaPlayerManager.play();
    }

    private void setSongsInformation()
    {
        songsCoverImage.setImageResource(currentSong.getCoverImageResID());
        songsNameText.setText(currentSong.getTitle());
        artistsNameText.setText(currentSong.getArtistId());
        seekBar.setMax(mediaPlayerManager.getMediaPlayer().getDuration());
        Palette.from(BitmapFactory.decodeStream(this.getContext().getResources().openRawResource(currentSong.getCoverImageResID()))).generate(palette -> {
            int vibrant = palette.getVibrantColor(Color.GRAY);
            miniPlayerBackground.setBackgroundTintList(ColorStateList.valueOf(vibrant));
        });
    }

    public void changePlaylist(Playlist playlist, int currentPosition) {
        this.playlist = playlist;
        this.currentPosition = currentPosition;
        this.currentSong = playlist.getSongsList().get(currentPosition);
        setSongsInformation();
        if(mediaPlayerManager != null)
        {
            mediaPlayerManager.clear();
            mediaPlayerManager = new MediaPlayerManager(getContext(), playlist.getSongsList(), currentPosition);
            mediaPlayerManager.play();

        }
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
    public void setMediaPlayerManager(MediaPlayerManager mediaPlayerManager){
        if(this.mediaPlayerManager!= null)
        {
            this.mediaPlayerManager.getMediaPlayer().release();
        }
        this.mediaPlayerManager = mediaPlayerManager;
    }
}