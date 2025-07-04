package com.example.manhinhappmusic.network;

public interface NetworkMessage {
    void onSuccess(String message);
    void onError(Throwable t);
}
