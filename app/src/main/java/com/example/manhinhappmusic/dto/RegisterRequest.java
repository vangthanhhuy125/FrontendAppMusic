package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class RegisterRequest {
 private String email;
 private String password;
 private String fullName;
 private String role; //"ROLE_USER" or "ROLE_ADMIN" or "ROLE_ARTIST"
}
