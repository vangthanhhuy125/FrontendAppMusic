package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private String role; // "ROLE_USER" or "ROLE_ADMIN" or "ROLE_ARTIST"

    public RegisterRequest(String email, String password, String fullName, String role){
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }
}
