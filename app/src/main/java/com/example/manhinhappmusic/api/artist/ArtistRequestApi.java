package com.example.manhinhappmusic.api.artist;

import com.example.manhinhappmusic.model.ArtistRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ArtistRequestApi {
    // Gửi yêu cầu trở thành artist
    @POST("/api/artist-requests")
    Call<ArtistRequest> submitRequest(@Body ArtistRequest request);

    // Lấy tất cả yêu cầu đang pending (admin)
    @GET("/api/artist-requests/pending")
    Call<List<ArtistRequest>> getPendingRequests();

    // Admin xét duyệt yêu cầu
    @PUT("/api/artist-requests/{id}/review")
    Call<ArtistRequest> reviewRequest(
            @Path("id") String id,
            @Body ArtistRequest request
    );
}
