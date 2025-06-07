package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;

import com.example.manhinhappmusic.network.ApiClient;

import java.util.List;

public interface AppRepository<T> {
    ApiClient apiClient = ApiClient.getInstance();
    LiveData<T> getItemById(String id);
    LiveData<List<T>> getAll();
}
