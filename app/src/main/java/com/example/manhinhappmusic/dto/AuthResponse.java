package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    public AuthResponse() {}
    public AuthResponse(String token) {
        this.token = token;
    }
}
