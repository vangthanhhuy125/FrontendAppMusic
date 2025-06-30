package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;

public interface AppRepository<T> {
    LiveData<T> getItemById(String id);
}
