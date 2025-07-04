package com.example.manhinhappmusic.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.manhinhappmusic.activity.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseFragment extends Fragment {

    protected int bottomNavVisibility = BottomNavigationView.VISIBLE;
    @Override
    public void onResume() {
        super.onResume();
        callback.setIsProcessing(true);
        if(getActivity() instanceof MainActivity )
        {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setBottomNavigationViewVisibility(bottomNavVisibility);
        }

    }

    public interface FragmentInteractionListener{
        boolean isProcessing = true;
        void onRequestChangeFragment(FragmentTag destinationTag, Object... params);
        void onRequestChangeFrontFragment(FragmentTag destinationTag, Object... params);
        void onRequestChangeActivity(FragmentTag destinationTag, Object... params);
        void onRequestOpenBottomSheetFragment(FragmentTag destinationTag, Object... params);
        void onRequestGoBackPreviousFragment();
        void onRequestLoadMiniPlayer();
        void setIsProcessing(boolean isProcessing);

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
        FORGOT_PASSWORD,
        EDIT_PROFILE,
        CONFIRM_DELETING_GENRE,
        CONFIRM_DELETING_SONG,
        CONFIRM_LOGGING_OUT,
        CHANGE_PASSWORD,
        OTP_VERIFICATION,
        ADD_PLAYLIST,
        LIBRARY_SEARCH,
        PLAYLIST_ADD_SONG,
        USER_GENRE,
        USER_ARTIST,
        USER_SEARCH_ADD_SONG,
        PLAYLIST_EDIT,
        ARTIST_DETAIL,
        USER_DETAIL,
        ADD_GENRE,
        CONFIRM_DELETING_USER,
        LIST_GENRE,
        EDIT_GENRE,
        ADMIN_HOME,
        ADMIN_PROFILE,
        EDIT_PROFILE_ADMIN,



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
