package com.example.manhinhappmusic.activity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.EditPlaylistFragment;
import com.example.manhinhappmusic.fragment.EditProfileFragment;
import com.example.manhinhappmusic.fragment.MiniPlayerFragment;
import com.example.manhinhappmusic.fragment.NowPlayingSongFragment;
import com.example.manhinhappmusic.fragment.SeacrhExFragment;
import com.example.manhinhappmusic.fragment.UserArtistFragment;
import com.example.manhinhappmusic.fragment.UserGenreFragment;
import com.example.manhinhappmusic.fragment.UserHomeFragment;
import com.example.manhinhappmusic.fragment.UserLibraryFragment;
import com.example.manhinhappmusic.fragment.UserLibrarySearchFragment;
import com.example.manhinhappmusic.fragment.UserPlaylistAddSongFragment;
import com.example.manhinhappmusic.fragment.UserPlaylistFragment;
import com.example.manhinhappmusic.fragment.UserProfileFragment;
import com.example.manhinhappmusic.fragment.UserSearchAddSongFragment;
import com.example.manhinhappmusic.fragment.UserSearchFragment;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.network.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {

    BottomSheetDialogFragment bottomSheetDialogFragment;
    BottomNavigationView bottomNavigationView;
//    AppFragmentFactory appFragmentFactory;
    MiniPlayerFragment miniPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getSupportFragmentManager().setFragmentFactory(appFragmentFactory);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        appFragmentFactory = new AppFragmentFactory(null, null, null, null);
//        appFragmentFactory.setLibrary(TestData.playlistList);
       // MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(this);

//loadFragment(new UserLibraryFragment());
//        MediaPlayer mediaPlayer = mediaPlayerManager.getMediaPlayer();
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .build();

//        mediaPlayer.setAudioAttributes(audioAttributes);        try
//        {
//            mediaPlayer.setDataSource("http://localhost:8081/api/artist/song/stream/683dabc3648d3b3112c873b0");
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
//                }
//            });
//            mediaPlayer.prepareAsync();
//        }
//        catch (Exception ex)
//        {
//            Log.println(Log.INFO, "eff", ex.getMessage());
//        }


        //loadFragment(new NowPlayingSongFragment());

        ApiClient apiClient = ApiClient.getInstance();
        apiClient.createAuthApi();
        apiClient.getApiService().login(new LoginRequest("23521766@gm.uit.edu.vn", "333")).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d("Login", response.body().getToken());
                apiClient.createAuthApiWithToken(response.body().getToken());
                apiClient.getApiService().getAllPlaylists().enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        Log.d("Playlist", Boolean.toString(response.isSuccessful()));
                        for(Playlist playlist: response.body())
                        {
                            Log.d("Playlist", playlist.getId());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable throwable) {
                        Log.e("Playlist", throwable.getMessage());

                    }
                });
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                Log.e("Login", throwable.getMessage());

            }
        });
        initializeView();
    }

    private void initializeView(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this::navigation);
    }

    private void loadMiniPlayer()
    {
        if (miniPlayer == null)
        {
            miniPlayer = new MiniPlayerFragment() ;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mini_player, miniPlayer)
                    .commit();
        }

    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean loadFrontFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.front_frame_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean navigation(MenuItem item){
        Fragment selectedFragment = null;

        try
        {
            if(item.getItemId() == R.id.nav_home)
                selectedFragment = new UserHomeFragment();
            else if(item.getItemId() == R.id.nav_search)
                selectedFragment = new UserSearchFragment();
            else if(item.getItemId() == R.id.nav_library)
            {
                selectedFragment = new UserLibraryFragment();


            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return loadFragment(selectedFragment);
    }


    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
        Fragment destinationFragment = null;
        if(destinationTag == BaseFragment.FragmentTag.USER_PROFILE)
            destinationFragment = new UserProfileFragment();
        else if(destinationTag == BaseFragment.FragmentTag.SEARCH_EX)
            destinationFragment = new SeacrhExFragment();
        else if(destinationTag == BaseFragment.FragmentTag.USER_PLAYLIST)
        {
            String id = null;
            if(params[0] instanceof String)
            {
                id = (String) params[0];
            }
            destinationFragment = UserPlaylistFragment.newInstance(id);

        }
        else if (destinationTag == BaseFragment.FragmentTag.LIBRARY_SEARCH) {
            destinationFragment = new UserLibrarySearchFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.PLAYLIST_ADD_SONG) {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserPlaylistAddSongFragment.newInstance(id);

        }
        else if(destinationTag == BaseFragment.FragmentTag.USER_GENRE)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserGenreFragment.newInstance(id);
        }
        else if(destinationTag == BaseFragment.FragmentTag.USER_ARTIST)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserArtistFragment.newInstance(id);
        }
//        else if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = UserSearchAddSongFragment.newInstance(id);
//        }



        loadFragment(destinationFragment);
    }

    @Override
    public void onRequestChangeFrontFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
        Fragment destinationFragment = null;
        if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserSearchAddSongFragment.newInstance(id);
        }
        else if(destinationTag == BaseFragment.FragmentTag.PLAYLIST_EDIT)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = EditPlaylistFragment.newInstance(id);
        }

        loadFrontFragment(destinationFragment);
    }

    @Override
    public void onRequestChangeActivity(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    @Override
    public void onRequestOpenBottomSheetFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

        if(destinationTag == BaseFragment.FragmentTag.EDIT_PROFILE)
        {
            bottomSheetDialogFragment = new EditProfileFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }
        else if(destinationTag == BaseFragment.FragmentTag.NOW_PLAYING_SONG)
        {
            bottomSheetDialogFragment = new NowPlayingSongFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }
//        else if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            bottomSheetDialogFragment = UserSearchAddSongFragment.newInstance(id);
//            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
//        }

    }

    @Override
    public void onRequestGoBackPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRequestLoadMiniPlayer() {
        loadMiniPlayer();
    }
}