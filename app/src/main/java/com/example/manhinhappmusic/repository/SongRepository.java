package com.example.manhinhappmusic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiService;

import java.io.File;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
public class SongRepository extends AppRepository {

    private static SongRepository instance;
    //    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();
    private Song currentSong;
    private SongResponse currentSongResponse;
    private SongRepository()
    {

//        songs.setValue(TestData.songList);
    }

    public static SongRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SongRepository();
        }

        return instance;
    }
    public LiveData<Song> getSong(String id) {
        MutableLiveData<Song> song = new MutableLiveData<>();

        apiClient.getApiService().getSongById(id).enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    song.setValue(response.body());
                }
                else
                {
                    Log.e("API", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable throwable) {

                if(callback != null)
                    callback.onError(throwable);
            }
        });
        return song;
    }

    public LiveData<List<Song>> getAll()
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        return songs;
    }


    public LiveData<List<Song>> searchSongs(String title)
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        apiClient.getApiService().searchSongs(title).enqueue(new Callback<List<SongResponse>>() {
            @Override
            public void onResponse(Call<List<SongResponse>> call, Response<List<SongResponse>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    List<Song> responseSongs = new ArrayList<>();
                    for(SongResponse songResponse: response.body())
                    {
                        responseSongs.add(songResponse.toSong());
                    }
                    songs.setValue(responseSongs);
                }

            }

            @Override
            public void onFailure(Call<List<SongResponse>> call, Throwable throwable) {

                if(callback != null)
                    callback.onError(throwable);
            }
        });

        return songs;
    }

    public LiveData<List<Song>> getRecentlySongs()
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        apiClient.getApiService().getRecentlySongs().enqueue((new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    songs.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable throwable) {
                callback.onError(throwable);
            }
        }));

        return songs;
    }

    public LiveData<List<SongResponse>> searchSongsWithStatus(String title)
    {
        MutableLiveData<List<SongResponse>> songs = new MutableLiveData<>();

        apiClient.getApiService().searchSongs(title).enqueue(new Callback<List<SongResponse>>() {
            @Override
            public void onResponse(Call<List<SongResponse>> call, Response<List<SongResponse>> response) {
                if(response.isSuccessful() && response.body() != null)
                {

                    songs.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<SongResponse>> call, Throwable throwable) {

                if(callback != null)
                    callback.onError(throwable);
            }
        });

        return songs;
    }

    public LiveData<List<String>> getPlaylistSongsBelongs(String id)
    {
        MutableLiveData<List<String>> playlistIds = new MutableLiveData<>();
        apiClient.getApiService().getPlaylistsSongBelongs(id).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    playlistIds.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable throwable) {

                if(callback != null)
                    callback.onError(throwable);
            }
        });
        return  playlistIds;
    }

    public LiveData<List<Song>> getSongByArtistId(String id)
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        apiClient.getApiService().getSongsByArtistId(id).enqueue((new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    songs.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable throwable) {
                callback.onError(throwable);
            }
        }));

        return songs;
    }

    public LiveData<ResponseBody> addSong(File audioFile, String title, String description, File thumbnailFile, List<String> genres)
    {
        MutableLiveData<ResponseBody> response = new MutableLiveData<>();

        MutableLiveData<ResponseBody> result = new MutableLiveData<>();
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody desPart = RequestBody.create(MediaType.parse("text/plain"), description);

        RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), thumbnailFile);
        MultipartBody.Part thumbnailPart = MultipartBody.Part.createFormData("coverImage", thumbnailFile.getName(), requestImageFile);
        RequestBody requestAudioFile = RequestBody.create(MediaType.parse("audio/mp3"), audioFile);
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData("file", audioFile.getName(), requestAudioFile);

        List<MultipartBody.Part> genreParts = new ArrayList<>();
        for (String genre : genres) {
            genreParts.add(MultipartBody.Part.createFormData(
                    "genreId", null,
                    RequestBody.create( MediaType.parse("text/plain"), genre)
            ));}

        apiClient.getApiService().addSong(audioPart, titlePart, desPart, thumbnailPart, null, null).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    result.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
        return response;
    }

}
