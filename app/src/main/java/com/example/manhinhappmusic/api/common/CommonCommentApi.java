package com.example.manhinhappmusic.api.common;

import com.example.manhinhappmusic.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommonCommentApi {
    // POST /api/common/comment/create
    @POST("/api/common/comment/create")
    Call<Comment> createComment(@Body Comment comment);

    // GET /api/common/comment
    @GET("/api/common/comment")
    Call<List<Comment>> getAllComments();

    // DELETE /api/common/comment/delete/{id}
    @DELETE("/api/common/comment/delete/{id}")
    Call<Void> deleteComment(@Path("id") String id);
}
