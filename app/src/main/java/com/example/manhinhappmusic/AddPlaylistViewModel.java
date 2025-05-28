package com.example.manhinhappmusic;

import androidx.lifecycle.ViewModel;

public class AddPlaylistViewModel extends ViewModel {
    private AddPlaylistFragment.OnCreateButtonClickListener onCreateButtonClickListener;

    public AddPlaylistFragment.OnCreateButtonClickListener getOnCreateButtonClickListener() {
        return onCreateButtonClickListener;
    }

    public void setOnCreateButtonClickListener(AddPlaylistFragment.OnCreateButtonClickListener onCreateButtonClickListener) {
        this.onCreateButtonClickListener = onCreateButtonClickListener;
    }
}
