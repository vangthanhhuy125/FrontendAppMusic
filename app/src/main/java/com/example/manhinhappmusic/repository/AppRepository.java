package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;

import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.NetworkMessage;

import java.util.List;

public abstract class AppRepository {

    public static void initialize(NetworkMessage networkMessage)
    {
        callback = networkMessage;
    }
    ApiClient apiClient = ApiClient.getInstance();

    protected static NetworkMessage callback;
}
