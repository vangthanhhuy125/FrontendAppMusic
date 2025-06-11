package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    public ForgotPasswordRequest(String email) {
        this.email = email;

    }
}
