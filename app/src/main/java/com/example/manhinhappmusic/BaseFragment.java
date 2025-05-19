package com.example.manhinhappmusic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    public interface FragmentInteractionListener{
        void onRequestChangeFragment(String destinationTag);
        void onRequestChangeActivity(String destinationTag);
        void onRequestOpenBottomSheetFragment(String destinationTag);
        void onRequestGoBackPreviousFragment();
    }

    protected FragmentInteractionListener callback;

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
