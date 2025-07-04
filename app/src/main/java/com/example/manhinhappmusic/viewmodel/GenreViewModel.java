package com.example.manhinhappmusic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.dto.GenreResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreViewModel extends ViewModel {
    private final MutableLiveData<List<Genre>> genreListLiveData = new MutableLiveData<>(new ArrayList<>());
    private final ApiService apiService = ApiClient.getApiService();

    public LiveData<List<Genre>> getGenreList() {
        return genreListLiveData;
    }

    // ✅ Gọi API để lấy danh sách Genre từ backend
    public void fetchGenres() {
        apiService.getAllGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Genre> genreList = new ArrayList<>();
                    for (Genre res : response.body()) {
                        genreList.add(new Genre(
                                res.getId(),
                                res.getName(),
                                res.getDescription(),
                                res.getThumbnailUrl()
                        ));
                    }
                    genreListLiveData.setValue(genreList);
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                // Log hoặc hiển thị lỗi nếu cần
            }
        });
    }

    // ✅ Gọi API để xóa genre
    public void deleteGenre(Genre genre) {
        apiService.deleteGenre(genre.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    List<Genre> currentList = genreListLiveData.getValue();
                    if (currentList != null) {
                        currentList.removeIf(g -> g.getId().equals(genre.getId()));
                        genreListLiveData.setValue(new ArrayList<>(currentList));
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log lỗi nếu cần
            }
        });
    }

    // ✅ Thêm mới genre vào danh sách hiện tại (khi tạo xong)
    public void addGenre(Genre genre) {
        List<Genre> currentList = genreListLiveData.getValue();
        if (currentList != null) {
            currentList.add(genre);
            genreListLiveData.setValue(new ArrayList<>(currentList));
        }
    }

    // ✅ Cập nhật genre đã chỉnh sửa
    public void updateGenre(Genre updatedGenre) {
        List<Genre> currentList = genreListLiveData.getValue();
        if (currentList != null) {
            for (int i = 0; i < currentList.size(); i++) {
                if (currentList.get(i).getId().equals(updatedGenre.getId())) {
                    currentList.set(i, updatedGenre);
                    genreListLiveData.setValue(new ArrayList<>(currentList));
                    return;
                }
            }
        }
    }
}
