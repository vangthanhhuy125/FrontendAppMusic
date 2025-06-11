package com.example.manhinhappmusic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {
 private String email;
 private String otp;
 private String newPassword;
}
