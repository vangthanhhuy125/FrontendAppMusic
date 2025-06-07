package com.example.manhinhappmusic.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    public interface FragmentInteractionListener{
        void onRequestChangeFragment(FragmentTag destinationTag, Object... params);
        void onRequestChangeFrontFragment(FragmentTag destinationTag, Object... params);

        void onRequestChangeActivity(FragmentTag destinationTag, Object... params);
        void onRequestOpenBottomSheetFragment(FragmentTag destinationTag, Object... params);
        void onRequestGoBackPreviousFragment();
        void onRequestLoadMiniPlayer();
    }

    protected FragmentInteractionListener callback;

    public static enum FragmentTag{
        USER_HOME,
        USER_SEARCH,
        USER_PLAYLIST,
        USER_PROFILE,
        USER_LIBRARY,
        REGISTER,
        SEARCH_EX,
        NOW_PLAYING_SONG,
        MINI_PLAYER,
        LOGIN,
        EDIT_PROFILE,
        CONFIRM_DELETING_GENRE,
        CONFIRM_DELETING_SONG,
        CONFIRM_LOGGING_OUT,
        CHANGE_PASSWORD,
        ADD_PLAYLIST,
        LIBRARY_SEARCH,
        PLAYLIST_ADD_SONG,
        USER_GENRE,
        USER_ARTIST,
        USER_SEARCH_ADD_SONG,
        PLAYLIST_EDIT,

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof FragmentInteractionListener){
            callback = (FragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException("Activity must implement FragmentInteractionListener");
        }
    }
}
