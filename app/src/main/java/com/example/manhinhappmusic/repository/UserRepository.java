package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.model.User;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository extends AppRepository {

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if(instance == null)
            instance = new UserRepository();
        return instance;
    }

    private UserRepository()
    {

    }

    public LiveData<User> getUserProfile(String token)
    {
        MutableLiveData<User> user = new MutableLiveData<>();
        apiClient.getApiService().getProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
        return  user;
    }

    public LiveData<User> editUserProfile(String token, String fullName, File avatar)
    {
        MutableLiveData<User> user = new MutableLiveData<>();
        RequestBody fullNamePart = RequestBody.create(MediaType.parse("text/plain"), fullName);

        File file = avatar;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        apiClient.getApiService().patchUser(fullNamePart, avatarPart).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                callback.onError(throwable);

            }
        });
        return user;
    }

}
