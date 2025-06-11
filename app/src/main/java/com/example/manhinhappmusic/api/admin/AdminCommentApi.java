package com.example.manhinhappmusic.api.admin;

import com.example.manhinhappmusic.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AdminCommentApi {
    @GET("/api/admin/comment")
    Call<List<Comment>> getAllComments();
}
