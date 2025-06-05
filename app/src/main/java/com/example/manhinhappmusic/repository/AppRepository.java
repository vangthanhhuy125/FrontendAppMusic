package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;

public interface AppRepository<T> {
    LiveData<T> getItemById(String id);
}
